import Foundation
import Shared
import WalletCore

final class SwiftWalletCoreBridge: NSObject, WalletCoreBridge {

    func generateMnemonic(strengthBits: Int32) -> String? {
        HDWallet(strength: strengthBits, passphrase: "")?.mnemonic
    }

    func isValidMnemonic(mnemonic: String) -> Bool {
        Mnemonic.isValid(mnemonic: mnemonic)
    }

    func deriveAddress(mnemonic: String, coin: String) -> String? {
        guard Mnemonic.isValid(mnemonic: mnemonic),
              let wallet = HDWallet(mnemonic: mnemonic, passphrase: "") else { return nil }
        switch coin {
        case DataConstants.shared.COIN_ETHEREUM: return wallet.getAddressForCoin(coin: .ethereum)
        default: return nil
        }
    }
}