package com.os.utils.ui;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DrawUtil {
   private boolean isRight;
   private boolean isBottomRight;
   private boolean isBottom;
   private int RESIZE_WIDTH = 10;
   private double MIN_WIDTH = 1000.0;
   private double MIN_HEIGHT = 580.0;

   public void addDrawFunc(Stage stage, Node root) {
      this.MIN_WIDTH = stage.getMinWidth();
      this.MIN_HEIGHT = stage.getMinHeight();
      root.setOnMouseDragReleased((event) -> {
         SetMouseEvent(stage, root, event);
      });
      root.setOnMouseReleased((event) -> {
         SetMouseEvent(stage, root, event);
      });
      root.setOnMouseMoved((event) -> {
         SetMouseEvent(stage, root, event);
      });
      root.setOnMouseDragged((event) -> {
         double x = event.getSceneX();
         double y = event.getSceneY();
         double nextX = stage.getX();
         double nextY = stage.getY();
         double nextWidth = stage.getWidth();
         double nextHeight = stage.getHeight();
         if (this.isRight || this.isBottomRight) {
            nextWidth = x;
         }

         if (this.isBottomRight || this.isBottom) {
            nextHeight = y;
         }

         if (nextWidth <= this.MIN_WIDTH) {
            nextWidth = this.MIN_WIDTH;
         }

         if (nextHeight <= this.MIN_HEIGHT) {
            nextHeight = this.MIN_HEIGHT;
         }

         stage.setX(nextX);
         stage.setY(nextY);
         stage.setWidth(nextWidth);
         stage.setHeight(nextHeight);
      });
   }

   private void SetMouseEvent(Stage stage, Node root, MouseEvent event) {
      event.consume();
      double x = event.getSceneX();
      double y = event.getSceneY();
      double width = stage.getWidth();
      double height = stage.getHeight();
      Cursor cursorType = Cursor.DEFAULT;
      this.isRight = this.isBottomRight = this.isBottom = false;
      if (y >= height - (double)this.RESIZE_WIDTH) {
         if (!(x <= (double)this.RESIZE_WIDTH)) {
            if (x >= width - (double)this.RESIZE_WIDTH) {
               this.isBottomRight = true;
               cursorType = Cursor.SE_RESIZE;
            } else {
               this.isBottom = true;
               cursorType = Cursor.S_RESIZE;
            }
         }
      } else if (x >= width - (double)this.RESIZE_WIDTH) {
         this.isRight = true;
         cursorType = Cursor.E_RESIZE;
      }

      root.setCursor(cursorType);
   }
}
