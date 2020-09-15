# ShowBrainImgeJavaFx

Add heatmap  into root
  Point2D[] events = detectedLoc(1);
  heatMap.addEvents(events);

  Rootpane.getChildren().addAll(viewPane, heatMap.getHeatMapImage());  
