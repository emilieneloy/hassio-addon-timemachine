ARG BUILD_FROM
FROM ${BUILD_FROM}

RUN apk add --no-cache \
    samba \
    samba-common-tools \
    avahi

RUN mkdir -p /var/run/avahi-daemon

COPY rootfs /

RUN chmod a+x \
    /etc/s6-overlay/s6-rc.d/init-smbd/run \
    /etc/s6-overlay/s6-rc.d/smbd/run \
    /etc/s6-overlay/s6-rc.d/smbd/finish \
    /etc/s6-overlay/s6-rc.d/avahi-daemon/run \
    /etc/s6-overlay/s6-rc.d/avahi-daemon/finish

HEALTHCHECK --interval=60s --timeout=10s --start-period=30s --retries=3 \
    CMD smbclient -L '\\localhost' -U '%' -m SMB3 2>&1 | grep -q 'TimeMachine\|timemachine' || exit 1
