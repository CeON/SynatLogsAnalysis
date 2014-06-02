#!/bin/sh

COLUMNFAMILIES="'c', 'm'"
HBASECOMMAND=""
for table in $*; do 
    HBASECOMMAND="$HBASECOMMAND \ndisable '$table' \ndrop '$table' \ncreate '$table', $COLUMNFAMILIES"
done

HBASECOMMAND="$HBASECOMMAND \nlist"

echo $HBASECOMMAND | hbase shell
