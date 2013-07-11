#!ipxe
kernel http://labs.uc.edu/tftp/nbi_img/live/vmlinuz
initrd http://labs.uc.edu/tftp/nbi_img/live/initrd.img
imgargs vmlinuz boot=live config noswap nolocales edd=on ocs_prerun1="dhclient -v eth0" ocs_prerun2="sleep 5" ocs_prerun3="mount -t nfs -o noatime,nodiratime labs.uc.edu:/home/partimag /home/partimag" ocs_live_run="/opt/drbl/sbin/ocs-live-restore" ocs_live_extra_param="--batch restoredisk ${image} sda" ocs_live_keymap="NONE" ocs_live_batch="yes" ocs_lang="en_US.UTF-8" nomodeset noprompt vga=788 ip=frommedia nosplash fetch=tftp://labs.uc.edu/live/filesystem.squashfs
boot

