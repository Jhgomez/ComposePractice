//
//  CommunityAdminApp.swift
//  CommunityAdmin
//
//  Created by Juan Enrique Gomez Flores on 9/01/24.
//

import SwiftUI
import shared

@main
struct CommunityAdminApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
