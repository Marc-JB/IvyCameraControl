import SwiftUI
import IvyCameraControl

@main
struct iOSApp: App {
    init() {
        StartupKt.onAppStartup()
    }
    
	var body: some Scene {
		WindowGroup {
            ComposeView()
                .ignoresSafeArea(.all)
		}
	}
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
