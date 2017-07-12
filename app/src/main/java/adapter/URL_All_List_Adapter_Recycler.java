package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lind.barcodescanner.MainActivity;
import com.bimbask.barcodescanner.R;

import java.util.ArrayList;
import java.util.HashMap;

public class URL_All_List_Adapter_Recycler extends RecyclerView.Adapter<URL_All_List_Adapter_Recycler.ItemViewHolder> {//},Filterable {
	private ArrayList<HashMap<String, String>> data;//,save_data;
	public URL_All_List_Adapter_Recycler(ArrayList<HashMap<String, String>> arrayList_total) {data=arrayList_total;}

	public long getItemId(int position) {return position;}


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_url_all_list, parent, false);
		ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder Holder, final int position) {
		final HashMap<String, String> data_local = data.get(Holder.getAdapterPosition());
		Holder.url_all_list_name.setText(data_local.get("name"));

		if( position==0) {
			Holder.handleView.setImageDrawable(Holder.handleView.getContext().getResources().getDrawable(R.drawable.ic_report));
		}else if( position==1) {
					Holder.handleView.setImageDrawable(Holder.handleView.getContext().getResources().getDrawable(R.drawable.ic_qr));
				}else if( position==2) {
			Holder.handleView.setImageDrawable(Holder.handleView.getContext().getResources().getDrawable(R.drawable.ic_invoice));
		}else if( position==3) {
			Holder.handleView.setImageDrawable(Holder.handleView.getContext().getResources().getDrawable(R.drawable.ic_documets));
		}
		Holder.v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent a = new Intent(view.getContext(), MainActivity.class);
				view.getContext().startActivity(a);
			}
		});
    }
	@Override
    public int getItemCount() {return data == null ? 0 : data.size();}


	class ItemViewHolder  extends RecyclerView.ViewHolder {
		View v;
		ImageView handleView;
		TextView url_all_list_name;
		RelativeLayout v_;
		public ItemViewHolder( View itemView) {
			super(itemView);
			this.v=itemView;
			this.handleView = (ImageView)v.findViewById(R.id.handle);
			this.url_all_list_name = (TextView)v.findViewById(R.id.url_all_list_name); // artist Name
		}

	}
}