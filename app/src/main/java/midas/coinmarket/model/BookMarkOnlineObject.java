package midas.coinmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookMarkOnlineObject implements Parcelable {
    String id;
    String content;
    String time;

    public BookMarkOnlineObject() {
    }

    public BookMarkOnlineObject(String id, String content, String time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }

    protected BookMarkOnlineObject(Parcel in) {
        id = in.readString();
        content = in.readString();
        time = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static final Creator<BookMarkOnlineObject> CREATOR = new Creator<BookMarkOnlineObject>() {
        @Override
        public BookMarkOnlineObject createFromParcel(Parcel in) {
            return new BookMarkOnlineObject(in);
        }

        @Override
        public BookMarkOnlineObject[] newArray(int size) {
            return new BookMarkOnlineObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(time);
    }
}
