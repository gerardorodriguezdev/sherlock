import SwiftUI

struct ContentView: View {
    @State private var images: [Int] = Array(1...2)
    
    var body: some View {
        NavigationView {
            VStack {
                HStack(alignment: .center, spacing: 8) {
                    TextField("Search...", text: .constant(""))
                        .textFieldStyle(.roundedBorder)
                    Button(action: {}) {
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
                .padding(.horizontal, 16)

                if images.isEmpty {
                    Text("No matches found")
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else {
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
            .navigationTitle("Sherlock")
        }
        
        //TODO: Separate into smaller pieces
        //TODO: Add bottom nav (Home)
        //TODO: Open image picker on image click
        //TODO: Process images on the background with reused code
        //TODO: Search queries + keep textfield text on enter
        //TODO: Do image big on click
        //TODO: Make image zoomable?
    }
}

#Preview {
    ContentView()
}
