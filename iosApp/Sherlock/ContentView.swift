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
    @State private var images: [Image] = []
    @State private var searchText: String = ""

    var body: some View {
        NavigationView {
            VStack {
                SearchSection(
                    images: $images,
                    searchText: $searchText
                )

                if images.isEmpty {
                    EmptyContent()
                } else {
                    LoadedContent(images: images)
                }
            }
            .navigationTitle("Sherlock")
        }
    }
}

private struct SearchSection: View {
    @Binding var images: [Image]
    @Binding var searchText: String

    @State private var photoPickerItems: [PhotosPickerItem] = []

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            TextField("Search...", text: $searchText)
                .textFieldStyle(.roundedBorder)

            PhotosPicker(
                selection: $photoPickerItems,
                matching: .images,
                photoLibrary: .shared()
            ) {
                ImageIcon()
            }
        }
        .padding(.horizontal, 16)
        .onChange(of: photoPickerItems) {
            Task {
                var loadedImages: [Image] = []

                for item in photoPickerItems {
                    if let data = try? await item.loadTransferable(
                        type: Data.self
                    ),
                        let uiImage = UIImage(data: data)
                    {
                        let image = Image(uiImage: uiImage)
                        loadedImages.append(image)
                    }
                }

                images = loadedImages
            }
        }
    }
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
    @State var images: [Image]

    var body: some View {
        ScrollView {
            LazyVGrid(
                columns: [
                    GridItem(.flexible(), spacing: 8),
                    GridItem(.flexible(), spacing: 8),
                ],
                spacing: 16
            ) {
                ForEach(0..<images.count, id: \.self) { index in
                    Button(action: {}) {
                        images[index]
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
