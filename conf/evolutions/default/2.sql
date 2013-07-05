# --- !Ups

CREATE ALIAS IF NOT EXISTS geodistance AS $$
 double geodistance(double lat1, double lon1, double lat2, double lon2) {
      double Radius = 6372797.560856;;
      double dLat = Math.toRadians(lat2-lat1);;
      double dLon = Math.toRadians(lon2-lon1);;
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) +  
         Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *  
         Math.sin(dLon/2) * Math.sin(dLon/2);;
      double c = 2 * Math.asin(Math.sqrt(a));; 
      return Radius * c;;
   }
$$;

# --- !Downs
