# Changelog

## 1.2.0

- Fix mDNS discovery: use standalone avahi-daemon (enable-dbus=no) with static service files
- Remove D-Bus dependency — avahi now runs independently of host D-Bus
- Publish _adisk._tcp, _smb._tcp, and _device-info._tcp for macOS auto-discovery
- Drop avahi-publish service in favor of avahi static services directory

## 1.0.0

- Initial release
- Time Machine backup support via Samba with vfs_fruit
- Configurable username, password, share name, and quota
