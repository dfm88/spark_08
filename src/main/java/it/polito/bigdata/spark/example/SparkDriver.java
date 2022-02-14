package it.polito.bigdata.spark.example;

import org.apache.spark.sql.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.functions.*;

public class SparkDriver {

	public static void main(String[] args) {

		String inputPath;
		String inputPath2;
		Double threshold;
		String outputFolder;

		inputPath = args[0];
		inputPath2 = args[1];
		threshold = Double.parseDouble(args[2]);
		outputFolder = args[3];

		// Create a Spark Session object and set the name of the application
//		SparkSession ss = SparkSession.builder().appName("Spark Lab #8 - Template").getOrCreate();

		// Invoke .master("local") to execute tha application locally inside Eclipse
		SparkSession ss = SparkSession.builder().master("local").appName("Spark Lab #8 - Template").getOrCreate();


		// * * * START * * *
		// load DataFrame
		Dataset<Row> registerDF = ss.read().format("csv")
				.option("delimiter", "\\t")
				.option("timestampFormat","yyyy-MM-dd HH:mm:ss")
				.option("header", true)
				.option("inferSchema", true)
				.load(inputPath).cache();
		Dataset<Row> stationsDF = ss.read().format("csv")
				.option("delimiter", "\\t")
				.option("header", true)
				.option("inferSchema", true)
				.load(inputPath2).cache();
		// Convert to Dataset
		Dataset<Register> registerDS = registerDF.as(Encoders.bean(Register.class)).cache();
		Dataset<Station> stationDS = stationsDF.as(Encoders.bean(Station.class)).cache();

		// filter dirty data (freeSlot = usedSlot = 0)
		Dataset<Register> registerDSfiltered = registerDS.filter(el->{
			return !(el.getFree_slots()==0 && el.getUsed_slots()==0);
		});

		System.out.println("show register DataSet");
		registerDF.schema();
		registerDSfiltered.show();

		//map the result..
		//+-------+-------------------+----------+----------+
		//|station|          timestamp|used_slots|free_slots|
		//+-------+-------------------+----------+----------+
		//|      1|2008-05-15 12:01:00|         0|        18|
		//|      1|2008-05-15 12:02:00|         0|        18|
		//.. to the same but timeStamp is a String "DayOfWeek_Hour" es "wed_15"
		//+-------+-------------------+----------+------+         ----
		//|station|          time_slot   | critical_occ | .. | total_occ | (filled after) .. | threshold (filled after)
		//+-------+----------------------+--------------+         ----
		//|      1|              "wed_15"|            1 | -> if free_slots==0
		//|      1|              "fri_11"|            0 | ->
		Dataset<RegisterTimeSlot> registerTimeSlotDS = registerDSfiltered.map(el->{
			int station_id = el.getStation();
			String weekDay = DateTool.DayOfTheWeek(el.getTimeStamp());
			String hour = String.valueOf(el.getTimeStamp().getHours());
			String time_slot = weekDay+"_"+hour;
			int critical_occ = el.getFree_slots() == 0 ? 1 : 0;
			return new RegisterTimeSlot(station_id, time_slot, critical_occ, 0);
		}, Encoders.bean(RegisterTimeSlot.class));
		System.out.println("Before Group by");
		registerTimeSlotDS.schema();
		registerTimeSlotDS.show();

		// now GROUP BY station, time_slot, sum(station_is_full), count(*)
		Dataset<Row> registerTimeSlotCompleteDF = registerTimeSlotDS.
				groupBy("station_id", "time_slot").
				agg(sum("critical_occ"), count("*")).
				withColumnRenamed("sum(critical_occ)", "critical_occ").
				withColumnRenamed("count(1)", "total_occ");
		// map GROUP BY result in DataSet
		Dataset<RegisterTimeSlot> registerTimeSlotCompleteDS =
				registerTimeSlotCompleteDF.as(Encoders.bean(RegisterTimeSlot.class));
		System.out.println("Group by");
		registerTimeSlotCompleteDS.schema();
		registerTimeSlotCompleteDS.show();

		// Calculate the threshold = critical_occ/total_occ and filter
		Dataset<RegisterPercentGeo> registerThresholdFiltDS = registerTimeSlotCompleteDS.flatMap(el ->{
			List<RegisterPercentGeo> results = new ArrayList<RegisterPercentGeo>();
			double critical_perc = (double) el.getCritical_occ()/ (double) el.getTotal_occ();
			String day_of_week = el.getTime_slot().split("_")[0];
			int hour = Integer.parseInt(el.getTime_slot().split("_")[1]);
			if(critical_perc>threshold){
				results.add(
						new RegisterPercentGeo(
								el.getStation_id(),
								day_of_week,
								hour,
								critical_perc,
								"",
								""
						)
				);
			}
			return results.iterator();
		}, Encoders.bean(RegisterPercentGeo.class));

		System.out.println("Threshold Filter");
		registerThresholdFiltDS.schema();
		registerThresholdFiltDS.show();

		// JOIN this result with stations
		Dataset<RegisterPercentGeo> registerStationJoinDS = registerThresholdFiltDS.
				join(stationDS,
						registerThresholdFiltDS.col("station").
								equalTo(stationDS.col("id"))).
				selectExpr("station", "day_of_week", "hour", "criticality", "longitude as longitude_ds", "latitude as latitude_ds")
				.as(Encoders.bean(RegisterPercentGeo.class)).cache();

		System.out.println("JOIN");
		registerStationJoinDS.schema();
		registerStationJoinDS.show();

		// Order result decreasing by criticality
		Dataset<RegisterPercentGeo> finalDS = registerStationJoinDS.sort(
				new Column("criticality").desc(),
				new Column("station"),
				new Column("day_of_week"),
				new Column("hour")
		).as(Encoders.bean(RegisterPercentGeo.class));

		System.out.println("FINAL");
		finalDS.schema();
		finalDS.show();


		// Save the result in the output folder
		finalDS.write().format("csv").option("header", true)
				.save(outputFolder);


		// Close the Spark session
		ss.stop();
	}
}
