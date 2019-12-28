package com.jalvaro.velomonaco.client.io;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.util.Log;

import com.jalvaro.velolibrary.client.application.Constants;
import com.jalvaro.velolibrary.client.exceptions.VeloException;
import com.jalvaro.velolibrary.client.io.Api;
import com.jalvaro.velolibrary.client.models.FleetVO;
import com.jalvaro.velolibrary.client.models.StationVO;
import com.jalvaro.velolibrary.client.utils.CRUDUtils;

public class VeloMonacoApi extends Api {

	private static final String TAG = VeloMonacoApi.class.getName();

	private static final String URL_1 = "https://cam.cleanenergyplanet.com/public/stations_poi.php?key=DMG346GLR567";

	@Override
	public FleetVO getFleet() throws VeloException {
		FleetVO fleetVO = null;

		fleetVO = parse(CRUDUtils.get(URL_1));

		return fleetVO;
	}

	private FleetVO parse(String response) throws VeloException {
		int i = 0;
		FleetVO fleetVO = new FleetVO();

		try {
		for (String station : response.split("\\n")) {
			i++;
			if (i != 1) {
				String[] stationSplitted = station.split("\\t");
				StationVO stationVO = new StationVO();
				stationVO.setLatitude(Double.valueOf(stationSplitted[0]));
				stationVO.setLongitude(Double.valueOf(stationSplitted[1]));
				stationVO.setId(Integer.valueOf(stationSplitted[2]));
				stationVO.setDescription(stationSplitted[3]);
				stationVO.setName("");
				stationVO.setAvailable(1);
				int freeSlots = Integer.valueOf(stationSplitted[7].substring(stationSplitted[7].lastIndexOf("c=") + 2,
						stationSplitted[7].lastIndexOf("&m=")));
				int totalSlots = Integer.valueOf(stationSplitted[7].substring(stationSplitted[7].lastIndexOf("&m=") + 3));
				stationVO.setTotalFreeSlots(freeSlots);
				stationVO.setTotalOccupiedSlots(totalSlots - freeSlots);
				stationVO.setTotalFunctionalSlots(totalSlots);
				stationVO.setTotalSlots(totalSlots);

				fleetVO.addStation(stationVO);
			}
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.JSON_TIME_FORMATTER, Locale.getDefault());
		fleetVO.setDate(dateFormat.format(Calendar.getInstance().getTime()));
		}catch(Exception e) {
			Log.e(TAG, "get - " + e.getMessage(), e);
			throw(new VeloException(VeloException.VELO_EXCEPTION_SERVER_ERROR));
		}

		return fleetVO;
	}

}
