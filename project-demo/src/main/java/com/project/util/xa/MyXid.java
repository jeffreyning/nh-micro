package com.project.util.xa;

import javax.transaction.xa.*;
public class MyXid implements Xid
{
protected int formatId;
protected byte gtrid[];
protected byte bqual[];
public MyXid()
{
}
@Override
public byte[] getBranchQualifier() {
	return bqual;
}
@Override
public int getFormatId() {
	return 0;
}
@Override
public byte[] getGlobalTransactionId() {
	return gtrid;
}
public MyXid(int formatId, byte gtrid[], byte bqual[])
{
this.formatId = formatId;
this.gtrid = gtrid;
this.bqual = bqual;
}

}
