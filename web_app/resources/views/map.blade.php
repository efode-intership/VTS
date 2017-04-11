<!DOCTYPE html>
<html>
  <head>
    <title>Schedule history map</title>
    <meta name="viewport" content="initial-scale=1.0">
    <meta charset="utf-8">
    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
  </head>
  <body>
    <div><p>hihi</p></div>
    <div id="map"></div>
    <script>
      var map;
      function initMap() {
        // var startPoint = {lat: -34.397, lng: 150.644};
        var startPoint = new google.maps.LatLng(-34.397, 150.644);
        // var endPoint = {lat: -34.397, lng: 150.654}
        var endPoint = new google.maps.LatLng(-34.397, 150.654);
        map = new google.maps.Map(document.getElementById('map'), {
          center: startPoint,
          zoom: 15
        });

        // map.data.setStyle(function(feature) {
        //   //  var magnitude = feature.getProperty('mag');
        //   var magnitude = 5.4;
        //    return {
        //      icon: getCircle(magnitude)
        //    };
        //  });
        //
        //  function getCircle(magnitude) {
        //     return {
        //       path: google.maps.SymbolPath.CIRCLE,
        //       fillColor: 'red',
        //       fillOpacity: .2,
        //       scale: Math.pow(2, magnitude) / 2,
        //       strokeColor: 'white',
        //       strokeWeight: .5
        //     };
        //   }
        var heatmapData = [];
        heatmapData.push(startPoint);
        heatmapData.push(endPoint);
        var heatmap = new google.maps.visualization.HeatmapLayer({
          data: heatmapData,
          dissipating: false,
          map: map
        });
        heatmap.set('radius', 5);
        // map.data.addGeoJson(startPoint);

        // var marker = new google.maps.Marker({
        //   position: startPoint,
        //   map: map
        // });
        // var marker = new google.maps.Marker({
        //   position: endPoint,
        //   map: map
        // });
      }
    </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBXvH-gMcH_ynmX_9bx3aXbdeneWv-UM4k&libraries=visualization&callback=initMap"
    async defer></script>
  </body>
</html>
