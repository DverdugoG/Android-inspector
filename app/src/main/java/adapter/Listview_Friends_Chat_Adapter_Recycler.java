package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimbask.barcodescanner.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Listview_Friends_Chat_Adapter_Recycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	 static final  int  TYPE_ITEM = 1;
	  ArrayList<HashMap<String, String>>data, save_data;
	public Listview_Friends_Chat_Adapter_Recycler(ArrayList<HashMap<String, String>> arrayList) {
    	save_data= arrayList;data=arrayList;
    }
	public int getCount() {return data.size();}
	public ArrayList<HashMap<String, String>> get_data() {return data;}
	public Object getItem(int position) {return position;}
	public long getItemId(int position) {return position;}

	@Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        	View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_friends, parent, false);
            return new RecyclerItemViewHolder_listview_adapter(view);
  }
  @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

	  RecyclerItemViewHolder_listview_adapter holder = (RecyclerItemViewHolder_listview_adapter) viewHolder;
			final HashMap<String, String> data_local = data.get(position);


   try{holder.set_songArtists(data_local.get("name"));
	   holder.set_Description(data_local.get("description"));
	  if( data_local.get("status").equals("yes")){
		  holder.imageview_status.setImageDrawable( holder.imageview_status.getContext().getResources().getDrawable(R.drawable.ok_station));
	   }if( data_local.get("status").equals("no")){
		   holder.imageview_status.setImageDrawable( holder.imageview_status.getContext().getResources().getDrawable(R.drawable.not_station));
	   }

   } catch(IndexOutOfBoundsException e){}
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    @Override
    public int getItemCount() {return data == null ? 0 : (data.size());}



	  class RecyclerItemViewHolder_listview_adapter extends RecyclerView.ViewHolder {
   		  TextView  artist,textView_description;
	      ImageView song_image, imageview_status;
	      View v;
	    public RecyclerItemViewHolder_listview_adapter(final View parent){//}, TextView artist, ImageView song_image,ImageView imageview_status) {
	        super(parent);
	        this.v=parent;
	       /** this.artist = artist;
	        this.song_image=song_image;
			this.imageview_status= imageview_status;**/
			this.   artist = (TextView)v.findViewById(R.id.songArtists); // artist Name
			this.  song_image= (ImageView)v.findViewById(R.id.image_listSong);
			this. imageview_status =(ImageView)v.findViewById(R.id.imageview_status);
			this.textView_description =(TextView)v.findViewById(R.id.textView_description);

		}
	    /**public  RecyclerItemViewHolder_listview_adapter newInstance(View v) {
			TextView   artist = (TextView)v.findViewById(R.id.songArtists); // artist Name
	    	ImageView  song_image= (ImageView)v.findViewById(R.id.image_listSong); 
          	ImageView imageview_status =(ImageView)v.findViewById(R.id.imageview_status);
	        return new RecyclerItemViewHolder_listview_adapter(v, artist,song_image,imageview_status);
	    }**/
	    public void set_songArtists(CharSequence text) {artist.setText(text);}
		public void set_Description(CharSequence txt){
			textView_description.setText(txt);
		}
		public void visible_imageview_status(){imageview_status.setVisibility(View.VISIBLE);}
		public void invisible_imageview_status(){imageview_status.setVisibility(View.INVISIBLE);}

/**
		  public void set_song_image(String KEY_MOBILE, String Signature){

			  if (Build.VERSION.SDK_INT >= 23) {
				  File folder = new File(Environment.getExternalStorageDirectory() + "/" + "bimbask" + "/" + "Friends" + "/" + KEY_MOBILE + "/");

				  if(folder.exists() && folder.canRead()) {


					  Glide.with(v.getContext())
							  .load(new File(Environment.getExternalStorageDirectory() + "/" + "bimbask" + "/" + "Friends" + "/" + KEY_MOBILE + "/" + KEY_MOBILE + ".jpg"))
							  .signature(new StringSignature(Signature))
							  .override(90, 90)
							  .placeholder(R.drawable.account_empty_photo).into(song_image);
				  }else{
					  Glide.with(v.getContext())
							  .load(new File(v.getContext().getApplicationContext().getFilesDir().getAbsolutePath() + "/" + "bimbask" + "/" + "Friends" + "/" + KEY_MOBILE + "/" + KEY_MOBILE + ".jpg"))
							  .signature(new StringSignature(Signature))
							  .override(90, 90)
							  .placeholder(R.drawable.account_empty_photo).into(song_image);
				  }


			  }else {
				  //if(picture.exists()) {
				  Glide.with(v.getContext())
						  .load(new File(Environment.getExternalStorageDirectory() + "/" + "bimbask" + "/" + "Friends" + "/" + KEY_MOBILE + "/" + KEY_MOBILE + ".jpg"))
						  .signature(new StringSignature(Signature))
						  .override(90, 90)
						  .placeholder(R.drawable.account_empty_photo).into(song_image);
				  //	}else{


				  //	}
			  }
	    }
**/


	//}

	}
}