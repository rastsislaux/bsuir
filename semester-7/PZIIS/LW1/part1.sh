#!/bin/bash

# 1. Создать группы пользователей
groupadd group_iit1
groupadd group_iit2

# 2-3. Создать пользователей и добавить их в группы
useradd -m -G group_iit1 iit11
useradd -m -G group_iit1 iit12
useradd -m -G group_iit2 iit21
useradd -m -G group_iit2 iit22

# 4. Предоставить пользователю iit21 административные привилегии
usermod -aG sudo iit21

# 5. Создать пользователя iit3
useradd -m iit3

# 6. Создать папку pzs
mkdir /home/pzs

# 7-11. Создать папки с различными правами доступа
mkdir /home/pzs/pzs11 && chmod 700 /home/pzs/pzs11
mkdir /home/pzs/pzs12 && chmod 070 /home/pzs/pzs12
mkdir /home/pzs/pzs13 && chmod 007 /home/pzs/pzs13
mkdir /home/pzs/pzs14 && chmod 777 /home/pzs/pzs14
mkdir /home/pzs/pzs15 && chmod 700 /home/pzs/pzs15 && chown root:root /home/pzs/pzs15

# 12. Сменить текущего пользователя на iit11
su - iit11 << EOF

# 13. Создать файлы с различными правами доступа
for dir in /home/pzs/pzs11 /home/pzs/pzs12 /home/pzs/pzs13 /home/pzs/pzs14; do
    touch $dir/file{11..15} $dir/file{21..25} $dir/file{31..35} $dir/file{41..45} $dir/file{51..55}
    
    chmod 400 $dir/file11
    chmod 600 $dir/file12
    chmod 200 $dir/file13
    chmod 700 $dir/file14
    chmod 100 $dir/file15
    
    chmod 040 $dir/file21
    chmod 060 $dir/file22
    chmod 020 $dir/file23
    chmod 070 $dir/file24
    chmod 010 $dir/file25
    
    chmod 004 $dir/file31
    chmod 006 $dir/file32
    chmod 002 $dir/file33
    chmod 007 $dir/file34
    chmod 001 $dir/file35
    
    chmod 444 $dir/file41
    chmod 666 $dir/file42
    chmod 222 $dir/file43
    chmod 777 $dir/file44
    chmod 111 $dir/file45
    
    chmod 400 $dir/file51 && chown root:root $dir/file51
    chmod 600 $dir/file52 && chown root:root $dir/file52
    chmod 200 $dir/file53 && chown root:root $dir/file53
    chmod 700 $dir/file54 && chown root:root $dir/file54
    chmod 100 $dir/file55 && chown root:root $dir/file55
    
    echo "read testVariable" > $dir/file15
    echo "read testVariable" > $dir/file25
    echo "read testVariable" > $dir/file35
    echo "read testVariable" > $dir/file45
    echo "read testVariable" > $dir/file55
    
    for file in $dir/file*; do
        if [[ ! $file =~ file[0-9]5 ]]; then
            echo 'echo "Hello World"' > $file
        fi
    done
done

# 14-16. Проверка прав доступа (эту часть нужно выполнять вручную)

EOF

# 17. Удаление созданных файлов, папок, пользователей и групп
rm -rf /home/pzs
userdel -r iit11
userdel -r iit12
userdel -r iit21
userdel -r iit22
userdel -r iit3
groupdel group_iit1
groupdel group_iit2
