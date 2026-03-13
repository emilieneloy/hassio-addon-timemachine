[global]
   min protocol = SMB2
   server string = Home Assistant Time Machine
   multicast dns register = no
   server role = standalone server
   log file = /dev/stdout
   log level = 1
   security = user
   ntlm auth = yes
   idmap config * : backend = tdb
   idmap config * : range = 3000-7999

   # Apple optimizations
   vfs objects = catia fruit streams_xattr
   fruit:aapl = yes
   fruit:nfs_aces = no
   fruit:model = TimeCapsule8,119
   fruit:metadata = stream
   fruit:posix_rename = yes
   fruit:wipe_intentionally_left_blank_rfork = yes
   fruit:delete_empty_adfiles = yes

[{{ .share_name }}]
   path = /share/timemachine
   valid users = {{ .username }}
   writable = yes
   browsable = yes

   # Time Machine support
   fruit:time machine = yes
   {{ if gt .max_size_gb 0 }}fruit:time machine max size = {{ .max_size_gb }}G{{ end }}

   # Locking (required for Time Machine reliability)
   kernel oplocks = no
   kernel share modes = no
   posix locking = no
   ea support = yes
