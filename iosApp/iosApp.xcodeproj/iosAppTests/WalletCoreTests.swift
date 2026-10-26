import XCTest
import Shared

class WalletCoreTests: XCTestCase {

    func testKnownVectorEthereumAddress() {
        let bridge = SwiftWalletCoreBridge()
        let phrase = "abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon about"
        XCTAssertEqual(bridge.deriveAddress(mnemonic: phrase, coin: "ETHEREUM"),
            "0x9858EfFD232B4033E47d90003D41EC34EcaEda94")
    }
}