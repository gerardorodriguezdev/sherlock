import SwiftUI

struct ContentView: View {
    var something = "Hi"
    
    var body: some View {
        VStack {
            Text(something)
                .foregroundColor(.red)
                .bold()
                .lineLimit(1)
                .padding(10)
                .border(.gray, width:4)
        }
    }
}

#Preview {
    ContentView()
}

