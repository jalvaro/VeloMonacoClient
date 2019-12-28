package com.jalvaro.velomonaco.client.application;

import com.jalvaro.velolibrary.client.application.VeloApp;
import com.jalvaro.velomonaco.client.io.VeloMonacoApi;

public class VeloMonacoApp extends VeloApp {

	public VeloMonacoApp() {
		super();
		setVeloApi(new VeloMonacoApi());
	}
}
