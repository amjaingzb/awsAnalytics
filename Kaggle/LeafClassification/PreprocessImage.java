import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;


import javax.imageio.ImageIO;
import java.awt.Point ;


class MyPoint extends Point {
  int relSequence ; 
  boolean marked;

  public MyPoint(int x,int y){
    super(x,y);
    relSequence=0;
    marked=false;
  }
  public String toString() { 
    return ("x,y,relSequence,marked="+this.x+","+this.y+","+relSequence+","+marked);
  }

}

public class PreprocessImage {

	String[][] result;
  static MyPoint gFirstEdgePoint = null ;
	
	public static void main(String[] args){
//		new PreprocessImage(args[0]);
     testMakeRelationInEdgePoints();
	}
  //curvePoints : output Parameter
	public static void makeRelationInEdgePoints(MyPoint v, HashMap edgeHashmap, MyPoint[] curvePoints)
  {

    v.marked = true ;
    System.out.println(v);
    /*Determine distance 1 co-ordinates aka neighbours*/
    int[] neighbourHashcode = new int[8];
    neighbourHashcode[0] = new Point (v.x-1,v.y-1).hashCode() ;
    neighbourHashcode[1] = new Point (v.x  ,v.y-1).hashCode() ;
    neighbourHashcode[2] = new Point (v.x+1,v.y-1).hashCode() ;
    neighbourHashcode[3] = new Point (v.x-1,v.y).hashCode() ;
    neighbourHashcode[4] = new Point (v.x+1,v.y).hashCode() ;
    neighbourHashcode[5] = new Point (v.x-1,v.y+1).hashCode() ;
    neighbourHashcode[6] = new Point (v.x  ,v.y+1).hashCode() ;
    neighbourHashcode[7] = new Point (v.x+1,v.y+1).hashCode() ;
    //Look for this point in the hash map
    MyPoint neighbourPoint = null ;
    for(int i =0 ; i< neighbourHashcode.length;i++){
      neighbourPoint = (MyPoint)edgeHashmap.get(neighbourHashcode[i]);
      if(neighbourPoint == null){
//        System.out.println("null neighbourPoint found");
        continue ;
      }
 //     System.out.println("some neighbourPoint found");
      if (!neighbourPoint.marked){
 //       System.out.println("some unmarked neighbourPoint found");
        neighbourPoint.relSequence = v.relSequence+1;
        curvePoints[neighbourPoint.relSequence]=neighbourPoint;
  //      System.out.println("Going into recrsion");
        makeRelationInEdgePoints(neighbourPoint,edgeHashmap,curvePoints);
        break;
      }
    }
    if(neighbourPoint == null) {
      /* Now either the curve is complete or it is broken */
      System.out.println("Now either the curve is complete or it is broken");
      return ;
    }

  }
	/*
	  Correct the name. This guy also produces the edge point hash map which we are going to use for further processing.
	*/
	public static HashMap makeImageFromEdgePoints(Point[] edgePoints,int edgePointCount, int width, int height , String outputFile){
	 return makeImageFromEdgePoints(edgePoints,edgePointCount, width, height , outputFile, 200 );
  }
	public static HashMap makeImageFromEdgePoints(Point[] edgePoints,int edgePointCount, int width, int height , String outputFile, int color)
  {
    HashMap<Integer,MyPoint> edgeHashmap = new HashMap<Integer,MyPoint>(edgePointCount);
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for(int i=0;i<edgePointCount;i++)
    {
      bi.setRGB(edgePoints[i].x, edgePoints[i].y,color);
      MyPoint p = new MyPoint(edgePoints[i].x,edgePoints[i].y);
      edgeHashmap.put(new Integer(p.hashCode()),p);
      if(gFirstEdgePoint==null)gFirstEdgePoint=p ;

    }
    try{
      ImageIO.write(bi, "PNG", new File(outputFile));
    }catch(IOException e){
      System.out.println(e.getMessage());
      edgeHashmap = null ;
    }
    return edgeHashmap ;

  }

  public static void testMakeRelationInEdgePoints()
  {
    int index = -1;
    MyPoint[] points = new MyPoint[100];
    /*
    points[++index]= new MyPoint(1,1);
    points[++index] = new MyPoint(1,2);
    points[++index] = new MyPoint(1,3);
    points[++index] = new MyPoint(2,3);
    points[++index] = new MyPoint(3,3);
    */
    points[++index] = new MyPoint(3,3);
    points[++index]= new MyPoint(1,1);
    points[++index] = new MyPoint(1,2);
    points[++index] = new MyPoint(1,3);
    points[++index] = new MyPoint(2,3);

    HashMap<Integer,MyPoint> edgeHashmap = new HashMap<Integer,MyPoint>(300);
    for(int i=-1;++i<=index;){
    edgeHashmap.put(new Integer(points[i].hashCode()),points[i]);
    }

    MyPoint[] curvePoints = new MyPoint[10];
    curvePoints[0] = points[0];
    makeRelationInEdgePoints(points[0], edgeHashmap, curvePoints);
    for(int i=0;i<3;i++)
    {
      //System.out.println(curvePoints[i]);
    }
  }

	public static void makeColorShadedCurveFromEdgePoints(Point[] edgePoints,int edgePointCount, int width, int height , String outputFile)
  {
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    int color = 0 ;

    for(int i=0;i<edgePointCount;i++)
    {
      bi.setRGB(edgePoints[i].x, edgePoints[i].y,100+(color++)%155);
      MyPoint p = new MyPoint(edgePoints[i].x,edgePoints[i].y);
      if(gFirstEdgePoint==null)gFirstEdgePoint=p ;

    }
    try{
      ImageIO.write(bi, "PNG", new File(outputFile));
    }catch(IOException e){
      System.out.println(e.getMessage());
    }

  }

	public PreprocessImage(String fileName)
	{
		int neighbourhood = 3;
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));

			int width = img.getWidth();
			int height = img.getHeight();
			int pixels[][] = new int[width][height];
			for(int i=0;i<width;i++){
				for(int j=0;j<height;j++){
				        int p = img.getRGB(i, j);
							  /* Using only red, since ours is a black and white image */
							  p = (p>>16) & 0xff;
							  if(p>0)p=1;
								pixels[i][j] = p;
								System.out.print(p+ " ");
        }
        System.out.println("");
      }
      Point edgePixels[] = new Point[width*height] ;
      int edgePixelsCounter=0;

      for(int i=0;i<width;i++){
        for(int j=0;j<height;j++){
          /*
          int center =pixels[i][j];
          boolean result = false;
          for(int n1=-1;n1<=1;n1++){
            for(int n2=-1;n2<=1;n2++){
              boolean isNeighbourhoodPixelExists = true;
              if(i+n1>width || j+n2>height ||i+n1<0||j+n2<0){neighcontinue;
              int neighbour = pixels[i+n1][j+n2];
              if(center!=neighbour) {result=true;}
            }
          }
          if(result)edgePixels[edgePixelsCounter++]=new Point(i,j) ;
        }
        */
				  try{
				  if(pixels[i][j]!=pixels[i-1][j-1]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}
				  try{
				  if(pixels[i][j]!=pixels[i+0][j-1]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}
				  try{
				  if(pixels[i][j]!=pixels[i+1][j-1]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}

				  try{
				  if(pixels[i][j]!=pixels[i-1][j]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}
				  try{
				  if(pixels[i][j]!=pixels[i+1][j]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}

				  try{
				  if(pixels[i][j]!=pixels[i-1][j+1]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}
				  try{
				  if(pixels[i][j]!=pixels[i+0][j+1]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}
				  try{
				  if(pixels[i][j]!=pixels[i+1][j+1]) {edgePixels[edgePixelsCounter++]=new Point(i,j) ; continue;}
          }catch (ArrayIndexOutOfBoundsException e) {;}

        }
      }
      for(int i=0;i<edgePixelsCounter;i++){
        System.out.println(edgePixels[i]);
      }
	   
     MyPoint curvePixels[] = new MyPoint[edgePixelsCounter] ;
	   curvePixels[0]=gFirstEdgePoint;
	   HashMap edgeHashmap = makeImageFromEdgePoints(edgePixels,edgePixelsCounter, width, height , "output.png");
	   makeRelationInEdgePoints(gFirstEdgePoint,edgeHashmap,curvePixels);
	   makeColorShadedCurveFromEdgePoints(curvePixels,edgePixelsCounter, width, height , "output_1.png");

			/*
			int argbComponents = 4;
			result = new String[width * height][neighbourhood*neighbourhood*4+2];
			for(int i=0;i<width;i++){
				//if(i % 10 == 0) System.out.println(i);
				for(int j=0;j<height;j++){
					result[i*height+j][(neighbourhood*neighbourhood*argbComponents)]=""+i;
					result[i*height+j][(neighbourhood*neighbourhood*argbComponents)+1]=""+j;
					for(int n1=0;n1<neighbourhood;n1++){
						for(int n2=0;n2<neighbourhood;n2++){
							int n1Adj = i + n1 - (neighbourhood-1)/2;
							int n2Adj = j + n2 - (neighbourhood-1)/2;
							if(n1Adj >0 && n1Adj <width && n2Adj >0 && n2Adj <height)
							{
								int p = img.getRGB(n1Adj, n2Adj);

							    //get alpha
							    int a = (p>>24) & 0xff;
							    //get red
							    int r = (p>>16) & 0xff;
							    //get green
							    int g = (p>>8) & 0xff;
							    //get blue
							    int b = p & 0xff;
							    //System.out.println(""+a+" "+r+" "+g+" "+b);
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)] = ""+a;
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)+1] = ""+r;
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)+2] = ""+g;
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)+3] = ""+b;
							}
							else
							{
								result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)] = "NA";
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)+1] = "NA";
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)+2] = "NA";
							    result[i*height + j][(n1*neighbourhood*argbComponents)+(n2*argbComponents)+3] = "NA";
							}
						}  
					}
				}  
			}
			BufferedWriter br = new BufferedWriter(new FileWriter("/home/java/ankit/srep/Analytics/preprocessedImage.csv"));
			StringBuilder sb = new StringBuilder();
			for (int i=0;i<result.length;i++) {
				for (int j=0;j<result[i].length;j++) {
					if(j!=0)sb.append(",");
					sb.append(result[i][j]);
				}
				sb.append(System.getProperty("line.separator"));
			}

			br.write(sb.toString());
			br.close();
			*/

		} catch (IOException e) {
		  System.out.println(e.getMessage());
		}
		
		

	}
}
