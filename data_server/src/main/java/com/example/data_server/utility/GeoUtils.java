package com.example.data_server.utility;

import static java.lang.Math.*;

public class GeoUtils
{

  public static final double EARTH_RADIUS_KM = 6378.1; // Approx Earth radius in km

  public static double calculateDistance(double lat1, double lon1, double lat2,
      double lon2)
  {
    double latDistance = toRadians(lat2 - lat1);
    double lonDistance = toRadians(lon2 - lon1);

    double a = sin(latDistance / 2) * sin(latDistance / 2)
        + cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(lonDistance / 2)
        * sin(lonDistance / 2);

    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    return EARTH_RADIUS_KM * c;
  }
}