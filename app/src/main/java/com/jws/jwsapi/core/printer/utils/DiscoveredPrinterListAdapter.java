package com.jws.jwsapi.core.printer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jws.jwsapi.R;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterBluetooth;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterNetwork;
import com.zebra.sdk.printer.discovery.DiscoveredPrinterUsb;

import java.util.ArrayList;
import java.util.List;

public class DiscoveredPrinterListAdapter extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Bitmap bluetoothIcon;
    protected Bitmap networkIcon;
    protected Bitmap usbIcon;
    protected List<DiscoveredPrinter> discoveredPrinters;

    public DiscoveredPrinterListAdapter(Context context) {
        super();

        mInflater = LayoutInflater.from(context);

        discoveredPrinters = new ArrayList<>();

        bluetoothIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt);
        networkIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt);
        usbIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.bt);
    }

    public int getCount() {
        return discoveredPrinters.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView firstLine;
        TextView secondLine;
        ImageView icon;

        @SuppressLint({"ViewHolder", "InflateParams"}) View retVal = mInflater.inflate(R.layout.item_list_item_with_image_and_two_lines, null);

        firstLine = retVal.findViewById(R.id.list_item_text_1);
        firstLine.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        secondLine = retVal.findViewById(R.id.list_item_text_2);
        icon = retVal.findViewById(R.id.list_item_image);

        DiscoveredPrinter printer = discoveredPrinters.get(position);
        String friendlyField = "";
        if (printer instanceof DiscoveredPrinterUsb) {
        	friendlyField = "USB Printer";
        	icon.setImageBitmap(usbIcon);
        } else if (printer instanceof DiscoveredPrinterBluetooth) {
        	friendlyField = ((DiscoveredPrinterBluetooth) printer).friendlyName;
        	icon.setImageBitmap(bluetoothIcon);
        } else if (printer instanceof DiscoveredPrinterNetwork) {
        	friendlyField = printer.getDiscoveryDataMap().get("DNS_NAME");
        	icon.setImageBitmap(networkIcon);
        }
        firstLine.setText(friendlyField);
        secondLine.setText(printer.address);
        return retVal;
    }

	public void addPrinter(DiscoveredPrinter printer) {
		discoveredPrinters.add(printer);
		notifyDataSetChanged();
	}
	
	public DiscoveredPrinter getPrinter(int index) {
		return discoveredPrinters.get(index);
	}

}