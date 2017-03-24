package com.jukusoft.libgdx.rpg.network.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;

/**
 * Created by Justin on 04.01.2017.
 */
public class NetMessage extends DefaultByteBufHolder {

    /** the message header length */
    public static final int HEADER_LENGHT = 16;

    /* Maximum Transmission Unit in bytes */
    public static final int MTU = 1200;

    protected final int eventID;

    protected final int version;

    protected final long timestamp;

    protected int contentLengthInBytes;

    /**
     * Constructor which creates a new message and
     * sets the type, version and content.
     *
     * @param eventID
     *            the message type
     * @param version
     *            the message version
     * @param content
     *            the message content
     */
    public NetMessage (final int eventID, final int version, final long timestamp, ByteBuf content) {
        super(content);

        this.eventID = eventID;
        this.version = version;
        this.timestamp = timestamp;

        this.contentLengthInBytes = content.readableBytes();
        //System.out.println("send " + contentLengthInBytes + " bytes.");
    }

    /**
     * Gets the type of this message.
     *
     * @return the type of this message
     */
    public int getEventID () {
        return this.eventID;
    }

    /**
     * Gets the version of this message.
     *
     * @return the version of this message
     */
    public int getVersion() {
        return this.version;
    }

    /**
    * gets the sender timestamp of this message
     *
     * @return unix timestamp on which message was sended
    */
    public long getTimestamp () {
        return this.timestamp;
    }

    public int getHeaderLenght () {
        return HEADER_LENGHT;
    }

    public int getContentLengthInBytes () {
        return this.contentLengthInBytes;
    }

    protected void setContentLengthInBytes (int length) {
        this.contentLengthInBytes = length;
    }

    @Override
    public NetMessage copy () {
        return new NetMessage(this.eventID, this.version, this.timestamp, this.content().copy());
    }

    public ByteBufHolder copyContent () {
        return super.copy();
    }

    public int getNumberOfFragmentsByContent () {
        return content().readableBytes() % MTU;
    }

    public boolean equals (NetMessage obj) {
        if (obj == null) {
            throw new NullPointerException("obj cannot be null.");
        }

        return this.eventID == obj.eventID && this.version == obj.version && this.timestamp == obj.timestamp && super.equals(obj);
    }

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof NetMessage)) {
            throw new IllegalArgumentException("You can only compare NetMessage instances.");
        }

        return this.equals((NetMessage) obj);
    }

}
