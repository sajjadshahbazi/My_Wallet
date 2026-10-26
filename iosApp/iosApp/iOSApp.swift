import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        IosKoinKt.startKoinIos(walletCoreBridge: SwiftWalletCoreBridge())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}