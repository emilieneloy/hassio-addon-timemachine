ARG BUILD_FROM
FROM ${BUILD_FROM}

# Install Samba with vfs_fruit support
RUN apk add --no-cache \
    samba \
    samba-common-tools

# Copy root filesystem
COPY rootfs /

# Make s6 scripts executable
RUN chmod a+x \
    /etc/s6-overlay/s6-rc.d/init-smbd/run \
    /etc/s6-overlay/s6-rc.d/smbd/run \
    /etc/s6-overlay/s6-rc.d/smbd/finish

# Healthcheck - verify Samba is responding
HEALTHCHECK --interval=60s --timeout=10s --start-period=30s --retries=3 \
    CMD smbclient -L '\\localhost' -U '%' -m SMB3 2>&1 | grep -q 'TimeMachine\|timemachine' || exit 1
