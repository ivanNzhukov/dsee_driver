package dsee.utils;

public enum Commands {
    //https://cdn-home-kijj1rxhj4ajds6mvlvhfn7wjsb65ljd.dseelab.com/download/docs/DseeLab-sdk-and-api.pdf

    START_DEVICE(0xB6, 0x01, 0x01, 0x00),
    STOP_DEVICE(0xB6, 0x01, 0x02, 0x00),
    BRIGHTNESS_SET(0xB6, 0x02, 0x00, 0x00),
    BRIGHT_INCREASE(0xB6, 0x02, 0x11, 0x00),
    BRIGHT_DECREASE(0xB6, 0x02, 0x12, 0x00),
    SET_DEVICE_AS_HOST(0xB6, 0x03, 0x01, 0x00),
    SET_DEVICE_AS_SLAVE(0xB6, 0x03, 0x02, 0x00),
    SET_DEVICE_TO_AP_MODE(0xB6, 0x04, 0x01, 0x00),
    PLAY_NEXT(0xB6, 0x05, 0x01, 0x00),
    PLAY_LAST(0xB6, 0x05, 0x02, 0x00),
    PAUSE(0xB6, 0x05, 0x03, 0x00),
    CONTINUE(0xB6, 0x05, 0x04, 0x00),
    PLAYBACK_FROM_BEGINNING(0xB6, 0x05, 0x05, 0x00),
    SWITCH_TO_N_VIDEO_AND_PAUSE(0xB6, 0x05, 0x06, 0x00),
    PAUSE_OR_RESTART_PLAY(0xB6, 0x05, 0x07, 0x00);


    private byte firstByte;
    private byte secondByte;
    private byte thirdByte;
    private byte fourthByte;

    Commands(int firstByte, int secondByte, int thirdByte, int fourthByte) {
        this.firstByte = (byte) firstByte;
        this.secondByte = (byte) secondByte;
        this.thirdByte = (byte) thirdByte;
        this.fourthByte = (byte) fourthByte;
    }

    public byte[] getCommand() {
        return new byte[]{firstByte, secondByte, thirdByte, fourthByte};
    }

    public byte getFirstByte() {
        return firstByte;
    }

    public void setFirstByte(byte firstByte) {
        this.firstByte = firstByte;
    }

    public byte getSecondByte() {
        return secondByte;
    }

    public void setSecondByte(byte secondByte) {
        this.secondByte = secondByte;
    }

    public byte getThirdByte() {
        return thirdByte;
    }

    public void setThirdByte(byte thirdByte) {
        this.thirdByte = thirdByte;
    }

    public byte getFourthByte() {
        return fourthByte;
    }

    public void setFourthByte(byte fourthByte) {
        this.fourthByte = fourthByte;
    }
}
