# Time Machine Add-on for Home Assistant

[![HA add-on](https://img.shields.io/badge/HA-add--on-blue)](https://www.home-assistant.io/addons/)

macOS Time Machine backup target for Home Assistant OS, using Samba with Apple's vfs_fruit extensions.

## Installation

1. Go to **Settings > Add-ons > Add-on Store** in your Home Assistant UI
2. Click the menu (top right) > **Repositories**
3. Add this URL:
   ```
   https://github.com/emilieneloy/hassio-addon-timemachine
   ```
4. Find **Time Machine** in the store and click **Install**
5. Set a password in the **Configuration** tab
6. Click **Start**

## Connect from macOS

The share should appear automatically in **System Settings > General > Time Machine > Add Backup Disk** via Bonjour/mDNS auto-discovery.

Alternatively, use the command line:

```bash
tmutil setdestination -a smb://timemachine:YOUR_PASSWORD@homeassistant.local/TimeMachine
```

## Requirements

- Home Assistant OS (HAOS) on aarch64 (RPi4, RPi5, ODROID, etc.)
- No other Samba add-on using port 445
- ext4 or btrfs filesystem on storage

See [DOCS.md](timemachine/DOCS.md) for full documentation.
