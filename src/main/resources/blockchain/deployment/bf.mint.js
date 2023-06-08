"use strict";
const ethers = require('ethers')
const {getTokenAddress} = require('../tool/mysql')
const erc777Contract = require('../ERC777.json');
const fcContract = require('../FC.json');

let sender = {}
let wallet = {}

async function buildContract(name, abi) {
    const addr = await getTokenAddress(name);
    console.log(`token ${name} addr ${addr}`)
    const contract = new ethers.Contract(addr, abi, wallet);
    contract._name = name;
    console.info(`mint, contract ${name} ${addr}`)
    return contract;
}
async function run() {
    let cfxUrl = process.env.EVM_RPC_URL;
    const cfx = ethers.getDefaultProvider(cfxUrl);
    wallet = new ethers.Wallet(process.env.BOOMFLOW_ADMIN_PRIVATE_KEY, cfx);
    sender = await wallet.getAddress();

    const EOS_contract = await buildContract('EOS', erc777Contract.abi);
    const KCoin_contract = await buildContract('KCoin', erc777Contract.abi);
    const USDT_contract = await buildContract('USDT', erc777Contract.abi);
    var assets = [
        EOS_contract,
        KCoin_contract,
        USDT_contract,
    ];

    const users = [
        '0x13f1102173449494e3c35d9bb5a2a6a251127247',
    ]
    const targetAddr = process.argv[2];
// mint for one address.
    if (targetAddr) {
        users.splice(0, users.length);
        users.push(targetAddr);
    }

    cfx.getTransactionCount(sender).then(async (nonce) => {
        nonce = parseInt(nonce.toString())
        console.info(`nonce is ${nonce}`)

        const tasks = []
        for (var i = 0; i < assets.length; i++) {
            for (var j = 0; j < users.length; j++) {
                const asset = assets[i];
                const userAddr = users[j];
                tasks.push(mintCFXToken(userAddr, "100000000000000000000000000", asset, nonce++).catch(err=>{
                    console.log(`mint ${asset._name} for ${userAddr} fail:`, err)
                }))
            }
        }
        await Promise.all(tasks);
        console.info(`minted.`)
    }).catch(err=>{
        console.log(`fail get next nonce. sender ${sender} #`, err)
    })
}
/*var assets = [
    erc777_token_1,
    erc777_token_2,
]

const users = [
    contract_addr.get("crcl_address_1"),
    contract_addr.get("crcl_address_2")
]

cfx.getNextNonce(sender).then(async (nonce) => {
    for (var i = 0; i < assets.length; i++) {
        mint(users[i], "10000000000000000000000000000", "0", assets[i], JSBI.add(nonce, JSBI.BigInt(i)))
    }
    
    await waitNonce(JSBI.add(nonce, JSBI.BigInt(assets.length)), sender)
})*/
//===============================================
// Mint
async function mintCFXToken(account, amount, token, nonce) {
    console.info(`mint ${token._name} for ${account}, nonce ${nonce}`);
    const zero = "0x0000000000000000000000000000000000000000";
    return token.mint(account.toLowerCase(), amount, "","", {nonce})
        .then(tx=>tx.wait()).catch(reject => {
            console.info(`mint fail ${token._name}.`, reject)
            // process.exit(2);
        })
}
async function mintFC(account, amount, token, nonce) {
    const txParams = buildTxParams(nonce);
    console.info(`mint ${token._name} for ${account}, nonce ${nonce}`)
    return token.mint(account.toLowerCase(), amount).sendTransaction(txParams).executed().catch(reject=>{
        console.info(`mint ${token._name} fail.`, reject)
        // process.exit(1);
    })
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

run()
    .then(()=>console.info('mint finished.'))
    .catch(err=>console.info(`mint fail`, err));