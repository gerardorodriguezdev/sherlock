import SwiftUI

struct ContentView: View {
    @State var images: [Int]
    
    var body: some View {
        NavigationView {
            VStack {
                SearchSection()

                if images.isEmpty {
                    EmptyContent()
                } else {
                    LoadedContent(images: images)
                }
            }
            .navigationTitle("Sherlock")
        }
        
        //TODO: Add bottom nav (Home)
        //TODO: Open image picker on image click
        //TODO: Process images on the background with reused code
        //TODO: Search queries + keep textfield text on enter
        //TODO: Do image big on click
        //TODO: Make image zoomable?
    }
}

private struct SearchSection : View {
    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            TextField("Search...", text: .constant(""))
                .textFieldStyle(.roundedBorder)
            Button(action: {}) {
                ImageIcon()
            }
        }
        .padding(.horizontal, 16)
    }
}

private struct ImageIcon: View {
    var body: some View {
        Image(systemName: "photo")
            .font(.system(size: 24))
            .foregroundColor(.white)
            .frame(width: 48, height: 48)
            .background {
                Circle()
                    .fill(Color.blue)
            }
    }
}

private struct EmptyContent : View {
    var body: some View {
        Text("No matches found")
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

private struct LoadedContent : View {
    @State var images: [Int]
    
    var body: some View {
        ScrollView {
            LazyVGrid(
                columns: [
                    GridItem(.flexible()),
                    GridItem(.flexible()),
                ],
                spacing: 8
            ) {
                ForEach(images, id: \.self) { _ in
                    Button(action: {}) {
                        Image(systemName: "photo")
                            .resizable()
                            .scaledToFill()
                    }
                }
            }
            .padding(.horizontal, 16)
        }
    }
}

#Preview("Empty") {
    ContentView(images: [])
}

#Preview("Loaded") {
    ContentView(images: [1,2])
}
