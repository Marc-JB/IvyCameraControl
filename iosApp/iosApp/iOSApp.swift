import UIKit
import IvyCameraControl

@main
class AppDelegate : UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        StartupKt.onAppStartup()
        let window = UIWindow()
        window.rootViewController = MainViewControllerKt.MainViewController()
        window.makeKeyAndVisible()
        return true
    }
}
