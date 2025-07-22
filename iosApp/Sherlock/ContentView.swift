import PhotosUI
import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            HomeView()
                .tabItem {
                    Label("Home", systemImage: "house")
                }
        }
    }
}

private struct HomeView: View {
    @State private var matchedImages: [Image] = []
    @State private var searchText: String = ""
    @State private var selectedImage: ImageWrapper? = nil

    var body: some View {
        NavigationView {
            VStack {
                SearchSection(
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

private struct FullScreenImage: View {
    @State var image: Image
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

private struct SearchSection: View {
    @Binding var matchedImages: [Image]
    @Binding var searchText: String

    @State private var selectedImages: [PhotosPickerItem] = []

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            TextField("Search...", text: $searchText)
                .textFieldStyle(.roundedBorder)

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
                var loadedImages: [Image] = []

                for selectedImage in selectedImages {
                    if let data = try? await selectedImage.loadTransferable(
                        type: Data.self
                    ),
                        let uiImage = UIImage(data: data)
                    {
                        let image = Image(uiImage: uiImage)
                        loadedImages.append(image)
                    }
                }

                matchedImages = loadedImages
            }
        }
    }
}

private struct ImageWrapper: Identifiable {
    let id = UUID()
    let image: Image
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
    @State var matchedImages: [Image]
    let onImageSelected: (Image) -> Void

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
        }.frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

#Preview("Empty") {
    ContentView()
}

#Preview("Loaded") {
    ContentView()
}
