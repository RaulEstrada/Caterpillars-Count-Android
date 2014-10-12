package jforce.com.caterpillarcount;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader.OnClickCardHeaderPopupMenuListener;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * Created by justinforsyth on 9/29/14.
 */
public class OrderCard extends Card{

    protected TextView headerTitleET;
    protected TextView cardET;

    private String order;
    private int length;
    private int count;
    private String notes;

    private HomeActivity activity;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public OrderCard(Context context, Intent data) {
        this(context, R.layout.card_order_inner_content, data);



    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public OrderCard(Context context, int innerLayout, Intent data) {
        super(context, innerLayout);
        init(context, data);
    }

    /**
     * Init
     */
    private void init(Context context, Intent data){

        activity = (HomeActivity) context;

        order = data.getStringExtra("order");
        length = data.getIntExtra("length", 0);
        count = data.getIntExtra("count", 0);
        notes = data.getStringExtra("notes");

        //General
        setSwipeable(true);
        setId(order + activity.getCardCount());

        //Header
        CardHeader header = new CardHeader(context);
        header.setTitle(order);

        //create onClickHeader
        CardHeader.OnClickCardHeaderPopupMenuListener headerListener = new OnClickCardHeaderPopupMenuListener(){
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.card_action_remove){
                    removeCard((Card) card);
                }
                if(id == R.id.card_action_edit){
                    //Edit the card
                }

            }
        };


        //Add a popup menu. This method set OverFlow button to visibile
        header.setPopupMenu(R.menu.card, headerListener);

        addCardHeader(header);

        //Thumbnail
        CardThumbnail thumb = new CardThumbnail(context);

        String[] array = activity.getResources().getStringArray(R.array.order_array);

        if(order.equals(array[0])){//Beetles
            thumb.setDrawableResource(R.drawable.beetle);
        }
        if(order.equals(array[1])){//Caterpillars
            thumb.setDrawableResource(R.drawable.caterpillar);

        }

        addCardThumbnail(thumb);

        //Set a OnClickListener listener
        setOnSwipeListener(new Card.OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                removeCard(card);

            }
        });
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        cardET = (TextView) parent.findViewById(R.id.card_text);
        Button buttonDown = (Button) parent.findViewById(R.id.card_button_down);
        Button buttonUp = (Button) parent.findViewById(R.id.card_button_up);


        if (cardET!=null) {
            cardET.setText(getMetaDataString());
        }
        if (buttonDown!=null) {
            buttonDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count > 1) {
                        count--;
                        cardET.setText(getMetaDataString());
                    }
                    else{
                        Toast toast = Toast.makeText(activity, "Count value must be at least 1", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
        }
        if (buttonUp!=null) {
            buttonUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    count++;
                    cardET.setText(getMetaDataString());
                }
            });
        }

    }

    public String getMetaDataString(){
        String string = "Length: " + Integer.toString(length) + " mm" +  "\n"
                + "Count: " + Integer.toString(count) + "\n"
                + "Notes: \"" + notes + "\"";

        return string;

    }

    public void removeCard(Card card){
        activity.setCardCount(activity.getCardCount() - 1);
        activity.getCardList().remove(card);
        activity.getCardArrayAdapter().notifyDataSetChanged();
        ListView cardList = (ListView) activity.findViewById(R.id.myList);
        activity.setListViewHeightBasedOnChildren(cardList);

        if (activity.getCardCount() == 0) {

            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.cards_linear_layout);

            TextView cardHint = new TextView(activity);
            cardHint.setText(R.string.ordercards_fragment_hint);
            cardHint.setId(R.id.order_card_hint_text);
            cardHint.setTextColor(Color.parseColor("#000000"));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            cardHint.setLayoutParams(params);
            layout.addView(cardHint);

        }
    }

}