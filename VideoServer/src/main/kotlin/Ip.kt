import java.net.InetAddress
import java.net.NetworkInterface

/**
 * Get the local IP address of the device.
 * @return The local IP address of the device or null if not found.
 */
fun getLocalIpAddress(): String? {
    val interfaces = NetworkInterface.getNetworkInterfaces()
    for (networkInterface in interfaces) {
        val addresses = networkInterface.inetAddresses
        for (address in addresses) {
            if (!address.isLoopbackAddress && address is InetAddress) {
                val hostAddress = address.hostAddress
                if (hostAddress.indexOf(':') < 0) { // Ignore IPv6 addresses
                    return hostAddress
                }
            }
        }
    }
    return null
}
