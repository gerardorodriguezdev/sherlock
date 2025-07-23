import SwiftUI
import shared

@main
struct SherlockApp: App {
    let textExtractor = TextExtractor.init(
        imageProcessor: ImageProcessor.init(),
        dispatchersProvider: DispatchersProviderKt.dispatchersProvider(),
        tracer: TracerKt.tracer(),
        entries: KotlinMutableDictionary<NSString, KotlinMutableSet<NSString>>()
    )

    var body: some Scene {
        WindowGroup {
            ContentView(textExtractor: textExtractor)
        }
    }
}
