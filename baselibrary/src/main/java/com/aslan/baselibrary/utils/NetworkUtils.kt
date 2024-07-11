package com.aslan.baselibrary.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.elvishew.xlog.Logger
import com.elvishew.xlog.XLog
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface


/**
 * 网络相关
 *
 * @author Aslan chenhengfei@yy.com
 * @date 2020/5/26
 */
object NetworkUtils {
    private val mLogger: Logger = XLog.tag("NetworkUtils").build()

    /**
     * 获取所有网络类型
     */
    fun getAllNetworkInfo(context: Context): Array<NetworkInfo> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.allNetworkInfo
    }

    /**
     * 获取所有网络类型
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getAllNetwork(context: Context): Array<Network> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.allNetworks
    }

    /**
     * 获取所有可用网络
     */
    fun getAllAvailableNetworkInfo(context: Context): MutableList<NetworkInfo> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val allNetworkInfo = connectivityManager.allNetworkInfo

        val list = mutableListOf<NetworkInfo>()
        allNetworkInfo.forEach {
            if (it != null && it.isAvailable) {
                list.add(it)
            }
        }
        return list
    }

    /**
     * 获取所有已连接网络
     */
    fun getAllConnectedNetworkInfo(context: Context): MutableList<NetworkInfo> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val allNetworkInfo = connectivityManager.allNetworkInfo

        val list = mutableListOf<NetworkInfo>()
        allNetworkInfo.forEach {
            if (it != null && it.isConnected) {
                list.add(it)
            }
        }
        return list
    }

    /**
     * 网络是否可用
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo?.isAvailable ?: false
        }
    }

    /**
     * 网络是否已连接
     */
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo?.isConnected ?: false
        }
    }

    /**
     * 网络信息
     *
     * @param networkType ConnectivityManager.TYPE_WIFI
     */
    fun getNetworkInfo(context: Context, networkType: Int): NetworkInfo? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (null == connectivityManager) {
            return null
        }

        val networkInfo = connectivityManager.getNetworkInfo(networkType)
        return networkInfo
    }

    /**
     * 网络是否可用
     *
     * @param networkType ConnectivityManager.TYPE_WIFI
     */
    fun isNetworkAvailable(context: Context, networkType: Int): Boolean {
        val networkInfo = getNetworkInfo(context, networkType)
        return (networkInfo != null && networkInfo.isAvailable)
    }

    /**
     * 网络是否可用
     *
     * @param transportType NetworkCapabilities.TRANSPORT_CELLULAR
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkAvailable2(context: Context, transportType: Int): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        val isAvailable = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        val hasTransport = networkCapabilities?.hasTransport(transportType) ?: false
        return isAvailable && hasTransport
    }

    /**
     * 网络是否已连接
     *
     * @param networkType ConnectivityManager.TYPE_WIFI
     */
    fun isNetworkConnected(context: Context, networkType: Int): Boolean {
        val networkInfo = getNetworkInfo(context, networkType)
        return (networkInfo != null && networkInfo.isConnected)
    }

    /**
     * 网络是否已连接
     *
     * @param transportType NetworkCapabilities.TRANSPORT_CELLULAR
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isNetworkConnected2(context: Context, transportType: Int): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        val isAvailable = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        val hasTransport = networkCapabilities?.hasTransport(transportType) ?: false
        return isAvailable && hasTransport
    }

    /**
     * 获取本地IP地址
     */
    fun getInetAddress(context: Context): MutableList<InetAddress> {
        val list = mutableListOf<InetAddress>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = connectivityManager?.activeNetwork
            val linkProperties = connectivityManager?.getLinkProperties(activeNetwork)
            if (linkProperties != null) {
                linkProperties.linkAddresses.forEach {
                    if (!it.address.isLoopbackAddress && it.address.isSiteLocalAddress) {
                        list.add(it.address)
                    }
                }
                return list
            }
        }

        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress && address.isSiteLocalAddress) {
                    list.add(address)
                }
            }
        }
        return list
    }

    /**
     * 获取本地IPv4地址
     */
    fun getInet4Address(context: Context): MutableList<Inet4Address> {
        val list = mutableListOf<Inet4Address>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = connectivityManager?.activeNetwork
            val linkProperties = connectivityManager?.getLinkProperties(activeNetwork)
            if (linkProperties != null) {
                linkProperties.linkAddresses.forEach {
                    if (it.address is Inet4Address && !it.address.isLoopbackAddress && it.address.isSiteLocalAddress) {
                        list.add(it.address as Inet4Address)
                    }
                }
                return list
            }
        }

        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (address is Inet4Address && !address.isLoopbackAddress && address.isSiteLocalAddress) {
                    list.add(address)
                }
            }
        }
        return list
    }

    /**
     * 获取本地IPv6地址
     */
    fun getInet6Address(context: Context): MutableList<Inet6Address> {
        val list = mutableListOf<Inet6Address>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val activeNetwork = connectivityManager?.activeNetwork
            val linkProperties = connectivityManager?.getLinkProperties(activeNetwork)
            if (linkProperties != null) {
                linkProperties.linkAddresses.forEach {
                    if (it.address is Inet6Address && !it.address.isLoopbackAddress && it.address.isSiteLocalAddress) {
                        list.add(it.address as Inet6Address)
                    }
                }
                return list
            }
        }

        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (address is Inet6Address && !address.isLoopbackAddress && address.isSiteLocalAddress) {
                    list.add(address)
                }
            }
        }
        return list
    }

    /**
     * 判断是否在同一个子网
     */
    fun isIpAddressInSubnet(ipAddress: InetAddress, subnetAddress: InetAddress, mask: InetAddress): Boolean {
        val inetAddressBytes = ipAddress.address
        val subnetBytes = subnetAddress.address
        val maskBytes = mask.address

        for (i in inetAddressBytes.indices) {
            val addressByte = inetAddressBytes[i].toInt() and 0xFF
            val subnetByte = subnetBytes[i].toInt() and 0xFF
            val maskByte = maskBytes[i].toInt() and 0xFF

            if ((addressByte and maskByte) != (subnetByte and maskByte)) {
                return false
            }
        }

        return true
    }
}
