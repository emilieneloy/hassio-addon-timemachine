# Time Machine Add-on for Home Assistant

Turns your Home Assistant OS device into a macOS Time Machine backup target using Samba with Apple's vfs_fruit extensions.

## Configuration

| Option | Default | Description |
|--------|---------|-------------|
| `username` | `timemachine` | SMB username for authentication |
| `password` | *(required)* | SMB password for authentication |
| `share_name` | `TimeMachine` | Name of the SMB share visible on your Mac |
| `max_size_gb` | `512` | Maximum backup size in GB (0 = unlimited) |

## Setup

### 1. Install the add-on

Add this repository URL in **Settings > Add-ons > Add-on Store > Menu (top right) > Repositories**:

```
https://github.com/emilieneloy/hassio-addon-timemachine
```

Then find "Time Machine" in the add-on store and install it.

### 2. Configure

Set at minimum a **password** in the add-on configuration tab. The default username is `timemachine`.

### 3. Start the add-on

Click **Start**. Check the **Log** tab for any errors.

### 4. Connect from your Mac

**Option A — System Settings (recommended):**

1. Open **System Settings > General > Time Machine**
2. Click **Add Backup Disk...**
3. Your HAOS device should appear as a network disk. Select it.
4. Enter the username and password you configured.

**Option B — Terminal (if the share doesn't appear automatically):**

```bash
tmutil setdestination -a smb://timemachine:YOUR_PASSWORD@homeassistant.local/TimeMachine
```

Replace `timemachine`, `YOUR_PASSWORD`, and `TimeMachine` with your configured values.

### 5. Verify

```bash
tmutil destinationinfo
```

Should show a network destination pointing to your HAOS device.

## Troubleshooting

### Mac can't find the share

The add-on advertises via Bonjour/mDNS automatically. If the share doesn't appear:

1. Verify your Mac and HAOS device are on the same subnet
2. Restart the add-on (Stop → Start) and wait 30 seconds
3. Open **System Settings > General > Time Machine > Add Backup Disk** again

If mDNS still fails, connect manually using the IP address:

```bash
tmutil setdestination -a smb://timemachine:PASSWORD@192.168.1.XXX/TimeMachine
```

### Backup fails immediately

Check the add-on logs for Samba errors. Common causes:
- Password not set (required)
- Another Samba add-on using port 445 (uninstall it or stop it first)
- Filesystem on the storage doesn't support extended attributes (ext4/btrfs required)

### First backup is very slow

This is expected. The RPi4's USB 2.0/3.0 and network throughput limit initial backup speed. First backup of a large disk can take 12-24 hours. Subsequent incremental backups are much faster.

### Quota enforcement

Set `max_size_gb` to limit how much disk space Time Machine can use. Set to `0` for unlimited. Time Machine will manage space within this quota, automatically pruning old backups when the limit is reached.

## Known Issues

- **Healthcheck false negative**: The Docker healthcheck looks for the string `timemachine` in the share list. If you set `share_name` to a value that doesn't contain "timemachine" (e.g. `Backups`), the HA UI may show the add-on as "unhealthy". Backups still work — this is cosmetic only.
- **Avahi interface list**: mDNS is broadcast on `end0`, `eth0`, and `wlan0` only (hardcoded in `/etc/avahi/avahi-daemon.conf`). If your HAOS device uses a different NIC name, mDNS won't broadcast. Check with `ip link` on the HAOS host and edit the avahi config inside the container if needed.

## Data location

Backups are stored at `/share/timemachine` inside HAOS. This path persists across add-on restarts and HAOS updates.

## Maintenance

### Checking for Samba security updates

Samba and avahi are installed from Alpine's package manager at build time. To check for pending updates inside the running container:

```bash
sudo docker exec addon_57949fa6_timemachine apk list -u samba
```

### Bumping the base image

The base image is pinned in `build.yaml` (`ghcr.io/home-assistant/aarch64-base:3.21`). To update:

1. Check for new tags at [ghcr.io/home-assistant/aarch64-base](https://github.com/home-assistant/docker-base/pkgs/container/aarch64-base)
2. Update the tag in `timemachine/build.yaml`
3. Bump `version` in `timemachine/config.yaml`
4. Commit and push — HAOS will detect the new version and offer an update

### After HAOS major updates

Rebuild the add-on after major HAOS upgrades to pick up Alpine package improvements: uninstall the add-on, remove and re-add the repository, then reinstall.

## Limitations

- **aarch64 only** (RPi4, RPi5, ODROID, etc.) — no x86 support in v1
- **Single user** — one username/password pair
- **No nmbd** — NetBIOS name resolution not provided; use IP or mDNS hostname
