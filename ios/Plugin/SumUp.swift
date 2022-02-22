import Foundation

@objc public class SumUp: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
