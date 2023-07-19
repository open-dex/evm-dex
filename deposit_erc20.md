### How to deposit ERC20 token

Unlike ERC777 tokens, ERC20 tokens do not call `tokensReceived` on recipient.
So, users must follow steps here to deposit:
1. call `approve(spender, amount)` function on ERC20 contract.
2. call `deposit(recipient, amount)` function on CRC-L contract.

#### How to get the related contract addresses
From DEX api [_get_currencies](https://open-dex.github.io/conflux-dex-docs/matchflow/conflux-dex-api.html#_get_currencies),
or [_get_currency](https://open-dex.github.io/conflux-dex-docs/matchflow/conflux-dex-api.html#_get_currency),
retrieve two addresses:
1. `contractAddress` CRC-L contract address.
2. `tokenAddress` ERC20 contract address.