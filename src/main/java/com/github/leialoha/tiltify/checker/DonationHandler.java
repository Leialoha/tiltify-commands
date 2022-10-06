package com.github.leialoha.tiltify.checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.github.leialoha.tiltify.Tiltify;
import com.github.leialoha.tiltify.checker.TiltifyActions.Type;
import com.github.leialoha.tiltify.requests.components.ComponentCampaign;
import com.github.leialoha.tiltify.requests.components.ComponentUserDonation;
import com.github.leialoha.tiltify.requests.data.TiltifyDataCampaign;
import com.github.leialoha.tiltify.requests.data.TiltifyDataCampaignDonations;

public class DonationHandler {
	
	private static DonationHandler instance;
	private final HashMap<String, TiltifyActions> actions = new HashMap<>();
	private final List<Integer> campaignIds = new ArrayList<>();
	private final HashMap<Integer, Integer> lastDonationId = new HashMap<>();
	private final HashMap<Integer, ComponentCampaign> campaignDatas = new HashMap<>();
	private static BukkitTask task;
	
	private final File dataFolder = new File(Tiltify.getPlugin(Tiltify.class).getDataFolder(), "data");

	private DonationHandler() {
		instance = this;
	}

	public static DonationHandler getInstance() {
		if (instance == null) new DonationHandler();
		startTask();
		return instance;
	}

	private static void startTask() {
		if (task == null) {
			task = Bukkit.getScheduler().runTaskTimer(Tiltify.getPlugin(Tiltify.class), new Runnable() {
				@Override
				public void run() {
					if (Tiltify.getTiltifyRequest() == null) task.cancel();
					else DonationHandler.getInstance().check();
				}
			}, 0L, 600L); // Run this task every 30 seconds
		}
	}



	private void check() {
		campaignIds.forEach((campId) -> {
			if (!Tiltify.isRunning) return;

			Bukkit.getScheduler().runTaskAsynchronously(Tiltify.getPlugin(Tiltify.class), new Runnable() {
				@Override
				public void run() {

					if (!campaignDatas.containsKey(campId)) {
						try {
							String url = "campaigns/" + campId;
	
							TiltifyDataCampaign campaignData = Tiltify.getTiltifyRequest().call(TiltifyDataCampaign.class, url);
							ComponentCampaign campaignComponent = campaignData.data;

							campaignDatas.put(campId, campaignComponent);
						} catch (IOException e) {}
					}

					try {
						if (!campaignDatas.containsKey(campId)) return;

						String url = "campaigns/" + campId + "/donations";
						if (lastDonationId.containsKey(campId)) url += ("?after=" + lastDonationId.get(campId));

						TiltifyDataCampaignDonations donations = Tiltify.getTiltifyRequest().call(TiltifyDataCampaignDonations.class, url);
						if (donations.data.size() != 0) {
							lastDonationId.put(campId, donations.data.get(0).id);
							for (int i = donations.data.size() - 1; i >= 0; i--) {
								ComponentUserDonation donation = donations.data.get(i);

								PlayerActions playerAction = PlayerHandler.getInstance().getPlayer(donation.name, campId);
								float lastDonation = playerAction.donatedAmount;
								playerAction.donatedAmount += donation.amount;

								ComponentCampaign campaignData = campaignDatas.get(campId);

								HashMap<String, Object> options = new HashMap<>();
								options.put("player", donation.name);
								options.put("amount", donation.amount);
								options.put("totalAmount", playerAction.donatedAmount);
								options.put("campaignName", campaignData.name);
								options.put("campaignId", campaignData.id);
								options.put("campaignUrl", campaignData.url);
								options.put("campaignOwner", campaignData.owner);
								options.put("campaignCurrent", campaignData.totalAmountRaised);
								options.put("campaignGoal", campaignData.fundraiserGoalAmount);
								// options.put("campaignCause", campaignData.name);
								// options.put("campaignEvent", campaignData.name);

								Optional<TiltifyActions> totalAmount = actions.values().stream().filter((tiltifyAction) -> {
									return (tiltifyAction.type == Type.TOTAL && tiltifyAction.amount > lastDonation && tiltifyAction.amount <= playerAction.donatedAmount);
								}).findFirst();
								if (totalAmount.isPresent()) totalAmount.get().run(options);
								
								Optional<TiltifyActions> onceAmount = actions.values().stream().filter((tiltifyAction) -> {
									return tiltifyAction.type == Type.ONCE;
								}).sorted((value1, value2) -> Double.compare(value2.amount, value1.amount)).findFirst();
								if (onceAmount.isPresent()) onceAmount.get().run(options);

							}
						}
					} catch (IOException | IllegalArgumentException e) {}
				}
			});
		});
	}

	public List<Integer> getCampaigns() {
		return campaignIds;
	}

	public void addCampaign(int id) {
		campaignIds.add(id);
	}

	public void removeCampaign(int id) {
		campaignIds.removeIf((campId) -> { return campId == id; });
	}

	public TiltifyActions getAction(String actionName) {
		return actions.getOrDefault(actionName, null);
	}

	public boolean hasAction(String actionName) {
		return actions.containsKey(actionName);
	}

	public void addAction(TiltifyActions action) {
		actions.put(action.name, action);
	}

	public void load() {
		if (!dataFolder.exists()) return;

		File file = new File(dataFolder, "donations.dat");
		if (!file.exists()) return;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			@SuppressWarnings("unchecked") 
			HashMap<Integer, Integer> data = (HashMap<Integer, Integer>) objectInputStream.readObject();
			lastDonationId.putAll(data);

			objectInputStream.close();
			fileInputStream.close();
			
		} catch (IOException | ClassNotFoundException e) {
			Tiltify.LOGGER.warning("Could not load data from " + file.getName());
			e.printStackTrace();
		}
	}

	public void save() {
		if (!dataFolder.exists()) dataFolder.mkdirs();
		File file = new File(dataFolder, "donations.dat");

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(lastDonationId);

			objectOutputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			Tiltify.LOGGER.warning("Could not save data to " + file.getName());
			e.printStackTrace();
		}
	}


}
