#!ipxe
:start
menu iPxe Menu
item --gap -- -------------------------------------- Clonezilla Live -------------------------------------
item drbl Clonezilla Live 
item mcast Clonezilla DISK Multicast 
item mcastPart Clonezilla PARTITION Multicast

item --gap -- ------------------------------------ Advanced Options ------------------------------------
item shell Enter iPxe Shell
item reboot Reboot
item 
choose --timeout 60000 --default reboot selected || goto cancel
set menu-timeout 0
goto \${selected}

:drbl
kernel http://labs.uc.edu/tftp/nbi_img/live/vmlinuz
initrd http://labs.uc.edu/tftp/nbi_img/live/initrd.img
imgargs vmlinuz boot=live config noswap nolocales edd=on nomodeset noprompt ocs_prerun1="dhclient -v eth0" ocs_prerun2="sleep 5" ocs_prerun3="mount -t nfs -o noatime,nodiratime labs.uc.edu:/home/partimag /home/partimag" ocs_live_run="/opt/drbl/sbin/ocs-sr -x" ocs_live_keymap="NONE" ocs_live_batch="no" ocs_lang="en_US.UTF-8" vga="788" ip="frommedia" nosplash fetch=tftp://labs.uc.edu/live/filesystem.squashfs
boot || goto failed

:failed
echo Boot failed, going back to the menu
goto start

:cancel
echo Menu canceled, dropping to a shell
goto shell

:shell
echo Type exit to get back to a menu
shell
set-timeout 0
set submenu-timeout 0
goto start

:extra
ocs_prerun1="dhclient -v eth0" ocs_prerun2="sleep 5" ocs_prerun3="mount -t nfs -o noatime,nodiratime labs.uc.edu:/home/partimag /home/partimag" ocs_live_run="/opt/drbl/sbin/ocs-live-restore" ocs_live_extra_param="--batch restoredisk ${image} sda" ocs_live_keymap="NONE" ocs_live_batch="yes" ocs_lang="en_US.UTF-8" 

:reboot
reboot

:mcast
kernel http://labs.uc.edu/tftp/nbi_img/live/vmlinuz
initrd http://labs.uc.edu/tftp/nbi_img/live/initrd.img
imgargs vmlinuz boot=live config noswap nolocales edd=on nomodeset noprompt ocs_prerun1="dhclient -v eth0" ocs_prerun2="sleep 5" ocs_prerun3="mount -t nfs -o noatime,nodiratime labs.uc.edu:/home/partimag /home/partimag" ocs_live_run="/opt/drbl/sbin/ocs-sr" ocs_live_extra_param="-icds -g auto -e1 auto -e2 -r --clone-hidden-data -p reboot --mcast-port 2232 multicast_restoredisk ask_user sda" ocs_live_keymap="NONE" ocs_live_batch="yes" ocs_lang="en_US.UTF-8" vga="788" ip="frommedia" nosplash fetch=tftp://labs.uc.edu/live/filesystem.squashfs
boot || goto failed


:mcastPart
kernel http://labs.uc.edu/tftp/nbi_img/live/vmlinuz
initrd http://labs.uc.edu/tftp/nbi_img/live/initrd.img
imgargs vmlinuz boot=live config noswap nolocales edd=on nomodeset noprompt ocs_prerun1="dhclient -v eth0" ocs_prerun2="sleep 5" ocs_prerun3="mount -t nfs -o noatime,nodiratime labs.uc.edu:/home/partimag /home/partimag" ocs_live_run="/opt/drbl/sbin/ocs-sr" ocs_live_extra_param="-icds -g auto -e1 auto -e2 -r --clone-hidden-data -p reboot --mcast-port 2232 -k multicast_restoreparts ask_user ask_user" ocs_live_keymap="NONE" ocs_live_batch="yes" ocs_lang="en_US.UTF-8" vga="788" ip="frommedia" nosplash fetch=tftp://labs.uc.edu/live/filesystem.squashfs
imgargs vmlinuz-pxe devfs=nomount drblthincli=off selinux=0 text 1  clientdir=node_root ocs_opt="-l en_US.UTF-8 -icds -g auto -e1 auto -e2 -r --clone-hidden-data -p reboot --mcast-port 2232 -k --batch multicast_restoreparts ask_user ask_user"
boot || goto failed

