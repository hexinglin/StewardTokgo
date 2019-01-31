package per.hxl.stewardtokgo.Chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

import per.hxl.stewardtokgo.Chat.bean.MessageItem;
import per.hxl.stewardtokgo.Chat.view.GifTextView;
import per.hxl.stewardtokgo.R;
import per.hxl.stewardtokgo.utils.TimeUtil;

/**
 * @desc发送消息的adapter
 * @author pangzf
 * @blog:http://blog.csdn.net/pangzaifei/article/details/43023625
 * @github:https://github.com/pangzaifei/zfIMDemo
 * @qq:1660380990
 * @email:pzfpang451@163.com
 */
public class MessageAdapter extends BaseAdapter {

    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");
    public static final int MESSAGE_TYPE_INVALID = -1;
    public static final int MESSAGE_TYPE_MINE_TETX = 0x00;
    public static final int MESSAGE_TYPE_MINE_IMAGE = 0x01;
    public static final int MESSAGE_TYPE_MINE_AUDIO = 0x02;
    public static final int MESSAGE_TYPE_OTHER_TEXT = 0x03;
    public static final int MESSAGE_TYPE_OTHER_IMAGE = 0x04;
    public static final int MESSAGE_TYPE_OTHER_AUDIO = 0x05;
    public static final int MESSAGE_TYPE_TIME_TITLE = 0x07;
    public static final int MESSAGE_TYPE_HISTORY_DIVIDER = 0x08;
    private static final int VIEW_TYPE_COUNT = 9;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MessageItem> mMsgList;


    private static String NOW_SHOW_TIME ="";
    private long mPreDate;


    public MessageAdapter(Context context, List<MessageItem> msgList) {
        this.mContext = context;
        mMsgList = msgList;
        mInflater = LayoutInflater.from(context);
    }

    public void removeHeadMsg() {
        if (mMsgList.size() - 10 > 10) {
            for (int i = 0; i < 10; i++) {
                mMsgList.remove(i);
            }
            notifyDataSetChanged();
        }
    }

    public void setmMsgList(List<MessageItem> msgList) {
        mMsgList = msgList;
        notifyDataSetChanged();
    }

    public void upDateMsg(MessageItem msg) {
        mMsgList.add(msg);
        notifyDataSetChanged();
    }

    public void upDateMsgByList(List<MessageItem> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                mMsgList.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ===============
        int type = getItemViewType(position);
        TextMessageHolder holder = null;
        if (null == convertView && null != mInflater) {
            holder = new TextMessageHolder();
            switch (type) {
                case MESSAGE_TYPE_MINE_TETX: {
                    convertView = mInflater.inflate(
                            R.layout.zf_chat_mine_text_message_item, parent, false);
                    holder = new TextMessageHolder();
                    convertView.setTag(holder);
                    fillTextMessageHolder((TextMessageHolder) holder,
                            convertView);
                    break;
                }

                case MESSAGE_TYPE_OTHER_TEXT: {
                    convertView = mInflater.inflate(
                            R.layout.zf_chat_other_text_message_item, parent, false);
                    holder = new TextMessageHolder();
                    convertView.setTag(holder);
                    fillTextMessageHolder((TextMessageHolder) holder,
                            convertView);
                    break;
                }

                default:
                    break;
            }
        } else {
            holder = (TextMessageHolder) convertView.getTag();
        }

        final MessageItem mItem = mMsgList.get(position);
        if (mItem != null) {
            int msgType = mItem.getMsgType();
            if (msgType == MessageItem.MESSAGE_TYPE_TEXT) {
                handleTextMessage((TextMessageHolder) holder, mItem, parent);

            }
        }

        return convertView;
    }

    private void handleTextMessage(final TextMessageHolder holder,
            final MessageItem mItem, final View parent) {
        handleBaseMessage(holder, mItem);

        // 文字
        holder.msg.insertGif(mItem.getMessage());
       
    }




    private void handleBaseMessage(TextMessageHolder holder,
            final MessageItem mItem) {
        String showtime = TimeUtil.getChatTime(mItem.getDate());
        holder.time.setText(showtime);
        if (NOW_SHOW_TIME.equals(showtime))
            holder.time.setVisibility(View.GONE);
        else {
            holder.time.setVisibility(View.VISIBLE);
            NOW_SHOW_TIME = showtime;
        }


        //todo
//        holder.head.setBackgroundResource(PushApplication.heads[mItem
//                .getHeadImg()]);
//         if (!isComMsg && !mSpUtil.getShowHead()) {
//         holder.head.setVisibility(View.GONE);
//         }
//         showTextOrVoiceOrImage(mItem, holder);
        holder.progressBar.setVisibility(View.GONE);
        holder.progressBar.setProgress(50);
        //todo
//         holder.head.setBackgroundResource(PushApplication.heads[mItem
//         .getHeadImg()]);

    }

    private void fillBaseMessageholder(TextMessageHolder holder,
            View convertView) {
        holder.head =convertView.findViewById(R.id.icon);
        holder.time =convertView.findViewById(R.id.datetime);
        // holder.msg = (GifTextView) convertView.findViewById(R.id.textView2);
        holder.rlMessage = (RelativeLayout) convertView
                .findViewById(R.id.relativeLayout1);
        // holder.ivphoto = (ImageView) convertView
        // .findViewById(R.id.iv_chart_item_photo);
        holder.progressBar = (ProgressBar) convertView
                .findViewById(R.id.progressBar1);
        // holder.voiceTime = (TextView) convertView
        // .findViewById(R.id.tv_voice_time);
        holder.flPickLayout = (FrameLayout) convertView
                .findViewById(R.id.message_layout);
    }

    private void fillTextMessageHolder(TextMessageHolder holder,
            View convertView) {
        fillBaseMessageholder(holder, convertView);
        holder.msg = (GifTextView) convertView.findViewById(R.id.textView2);
    }




    private class TextMessageHolder {
        ImageView head;
        TextView time;
        ProgressBar progressBar;
        RelativeLayout rlMessage;
        FrameLayout flPickLayout;
        /**
         * 文字消息体
         */
        GifTextView msg;
    }


    /**
     * 取名字f010
     * 
     * @param faceName
     */
    private CharSequence options(String faceName) {
        int start = faceName.lastIndexOf("/");
        CharSequence c = faceName.subSequence(start + 1, faceName.length() - 4);
        return c;
    }


    @Override
    public int getItemViewType(int position) {
        // logger.d("chat#getItemViewType -> position:%d", position);
        try {
            if (position >= mMsgList.size()) {
                return MESSAGE_TYPE_INVALID;
            }

            MessageItem item = mMsgList.get(position);
            if (item != null) {
                boolean comMeg = item.isComMeg();
                int type = item.getMsgType();
                if (comMeg) {
                    // 接受的消息
                    switch (type) {
                        case MessageItem.MESSAGE_TYPE_TEXT: {
                            return MESSAGE_TYPE_OTHER_TEXT;
                        }

                        case MessageItem.MESSAGE_TYPE_IMG: {
                            return MESSAGE_TYPE_OTHER_IMAGE;
                        }

                        case MessageItem.MESSAGE_TYPE_RECORD: {
                            return MESSAGE_TYPE_OTHER_AUDIO;
                        }

                        default:
                            break;
                    }
                } else {
                    // 发送的消息
                    switch (type) {
                        case MessageItem.MESSAGE_TYPE_TEXT: {
                            return MESSAGE_TYPE_MINE_TETX;

                        }

                        case MessageItem.MESSAGE_TYPE_IMG: {
                            return MESSAGE_TYPE_MINE_IMAGE;

                        }

                        case MessageItem.MESSAGE_TYPE_RECORD: {
                            return MESSAGE_TYPE_MINE_AUDIO;
                        }

                        default:
                            break;
                    }
                }
            }
            return MESSAGE_TYPE_INVALID;
        } catch (Exception e) {
            Log.e("fff", e.getMessage());
            return MESSAGE_TYPE_INVALID;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

}