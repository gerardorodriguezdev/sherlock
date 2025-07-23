import PhotosUI
import SwiftUI
import shared

typealias SharedImage = shared.Image

struct ContentView: View {
    let textExtractor: TextExtractor
    var body: some View {
        TabView {
            HomeView(textExtractor: textExtractor)
                .tabItem {
                    Label("Home", systemImage: "house")
                }
        }
    }
}

private struct HomeView: View {
    let textExtractor: TextExtractor
    @State private var matchedImages: [SwiftUI.Image] = []
    @State private var searchText: String = ""
    @State private var selectedImage: ImageWrapper? = nil

    var body: some View {
        NavigationView {
            VStack {
                SearchSection(
                    textExtractor: textExtractor,
                    matchedImages: $matchedImages,
                    searchText: $searchText
                )

                if matchedImages.isEmpty {
                    EmptyContent()
                } else {
                    LoadedContent(
                        matchedImages: matchedImages,
                        onImageSelected: { image in
                            selectedImage = ImageWrapper(image: image)
                        }
                    )
                }
            }
            .navigationTitle("Sherlock")
            .sheet(
                item: $selectedImage,
                content: { selectedImage in
                    FullScreenImage(image: selectedImage.image)
                }
            )
        }
    }
}

private struct SearchSection: View {
    let textExtractor: TextExtractor
    @Binding var matchedImages: [SwiftUI.Image]
    @Binding var searchText: String

    @State private var selectedImages: [PhotosPickerItem] = []

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            TextField("Search...", text: $searchText)
                .textFieldStyle(.roundedBorder)
                .onSubmit {
                    Task {
                        let matchedKeys = try await textExtractor.search(
                            text: searchText
                        )
                        let newMatchedPhotosPickerItems = selectedImages.filter
                        {
                            selectedImage in
                            if let key = selectedImage.itemIdentifier {
                                matchedKeys.contains(key)
                            } else {
                                false
                            }
                        }
                        var newMatchedImages: [SwiftUI.Image] = []
                        for newMatchedPhotosPickerItem
                            in newMatchedPhotosPickerItems
                        {
                            if let data =
                                try? await newMatchedPhotosPickerItem
                                .loadTransferable(
                                    type: Data.self
                                ),
                                let uiImage = UIImage(data: data)
                            {
                                newMatchedImages.append(Image(uiImage: uiImage))
                            }
                        }
                        matchedImages = newMatchedImages
                    }
                }

            PhotosPicker(
                selection: $selectedImages,
                matching: .images,
                photoLibrary: .shared()
            ) {
                ImageIcon()
            }
        }
        .padding(.horizontal, 16)
        .onChange(of: selectedImages) {
            Task {
                var imagesToProcess: [SharedImage] = []

                for selectedImage in selectedImages {
                    if let data = try? await selectedImage.loadTransferable(
                        type: Data.self
                    ),
                        let uiImage = UIImage(data: data),
                        let key = selectedImage.itemIdentifier
                    {
                        imagesToProcess.append(
                            SharedImage(key: key, uiImage: uiImage)
                        )
                    }
                }

                try await textExtractor.processImages(
                    images: imagesToProcess
                )
            }
        }
    }
}

private struct ImageWrapper: Identifiable {
    let id = UUID()
    let image: SwiftUI.Image
}

private struct ImageIcon: View {
    var body: some View {
        Image(systemName: "photo")
            .font(.system(size: 24))
            .foregroundColor(.accentColor)
            .frame(width: 48, height: 48)
            .symbolRenderingMode(.multicolor)
    }
}

private struct EmptyContent: View {
    var body: some View {
        Text("No matches found")
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

private struct LoadedContent: View {
    @State var matchedImages: [SwiftUI.Image]
    let onImageSelected: (SwiftUI.Image) -> Void

    var body: some View {
        ScrollView {
            LazyVGrid(
                columns: [
                    GridItem(.flexible(), spacing: 8),
                    GridItem(.flexible(), spacing: 8),
                ],
                spacing: 16
            ) {
                ForEach(0..<matchedImages.count, id: \.self) { index in
                    Button(action: {
                        onImageSelected(matchedImages[index])
                    }) {
                        matchedImages[index]
                            .resizable()
                            .frame(
                                minWidth: 0,
                                maxWidth: .infinity,
                                minHeight: 0,
                                maxHeight: .infinity
                            )
                            .clipped()
                            .aspectRatio(1, contentMode: .fit)
                    }
                }
            }
            .padding(.horizontal, 16)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

private struct FullScreenImage: View {
    @State var image: SwiftUI.Image
    @Environment(\.dismiss) private var dismiss

    @State private var scale: CGFloat = 1.0
    @State private var lastScale: CGFloat = 1.0

    var body: some View {
        ZStack(alignment: .topTrailing) {
            image
                .resizable()
                .scaledToFit()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .scaleEffect(scale)
                .gesture(
                    MagnificationGesture()
                        .onChanged { value in
                            scale = lastScale * value
                        }
                        .onEnded { value in
                            lastScale = scale
                        }
                )
                .animation(.easeInOut(duration: 0.2), value: scale)
                .edgesIgnoringSafeArea(.all)

            Button(action: { dismiss() }) {
                Image(systemName: "xmark.circle.fill")
                    .foregroundColor(.black)
                    .font(.largeTitle)
                    .padding()
            }
        }
    }
}
