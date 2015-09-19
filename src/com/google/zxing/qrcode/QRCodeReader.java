/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.qrcode;

import java.util.List;
import java.util.Map;

import android.graphics.Point;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData;
import com.google.zxing.qrcode.detector.Detector;

/**
 * This implementation can detect and decode QR Codes in an image.
 *
 * @author Sean Owen
 */
public class QRCodeReader implements Reader {

  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

  private final Decoder decoder = new Decoder();

  protected final Decoder getDecoder() {
    return decoder;
  }

  /**
   * Locates and decodes a QR code in an image.
   *
   * @return a String representing the content encoded by the QR code
   * @throws NotFoundException if a QR code cannot be found
   * @throws FormatException if a QR code cannot be decoded
   * @throws ChecksumException if error correction fails
   */
  @Override
  public Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
    return decode(image, null);
  }

  @Override
  public final Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException {
    DecoderResult decoderResult;
    ResultPoint[] points;
    int w = 0,h = 0 ;
    if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
      BitMatrix bits = extractPureBits(image.getBlackMatrix());
      decoderResult = decoder.decode(bits, hints);
      w = bits.getWidth();
      h = bits.getHeight();
      points = NO_POINTS;
    } else {
      DetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect(hints);
      decoderResult = decoder.decode(detectorResult.getBits(), hints);
      w = detectorResult.getBits().getWidth();
      h = detectorResult.getBits().getHeight();
      points = detectorResult.getPoints();
    }

    // If the code was mirrored: swap the bottom-left and the top-right points.
    if (decoderResult.getOther() instanceof QRCodeDecoderMetaData) {
      ((QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(points);
    }

    Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.QR_CODE);
    List<byte[]> byteSegments = decoderResult.getByteSegments();
    if (byteSegments != null) {
      result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
    }
    String ecLevel = decoderResult.getECLevel();
    if (ecLevel != null) {
      result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
    }
    if (decoderResult.hasStructuredAppend()) {
      result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE,
                         decoderResult.getStructuredAppendSequenceNumber());
      result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY,
                         decoderResult.getStructuredAppendParity());
    }
    
    Point point = new Point(w,h);
    result.putMetadata(ResultMetadataType.OTHER, getVersionAndSize(point));
    
    return result;
  }

  @Override
  public void reset() {
    // do nothing
  }

  public Point getVersionAndSize(Point qrsize){
	  if(qrsize.x==0 || qrsize.y==0 || qrsize.x!=qrsize.y){
		  return new Point(0,25);
	  }else if (qrsize.x >= 21 && qrsize.x < 25){
		  return new Point(1,qrsize.x);
	  }else if (qrsize.x >= 25 && qrsize.x < 29){
		  return new Point(2,qrsize.x);
	  }else if (qrsize.x >= 29 && qrsize.x < 33){
		  return new Point(3,qrsize.x);
	  }else if (qrsize.x >= 33 && qrsize.x < 37){
		  return new Point(4,qrsize.x);
	  }else if (qrsize.x >= 37 && qrsize.x < 41){
		  return new Point(5,qrsize.x);
	  }else if (qrsize.x >= 41 && qrsize.x < 45){
		  return new Point(6,qrsize.x);
	  }else if (qrsize.x >= 45 && qrsize.x < 49){
		  return new Point(7,qrsize.x);
	  }else if (qrsize.x >= 49 && qrsize.x < 53){
		  return new Point(8,qrsize.x);
	  }else if (qrsize.x >= 53 && qrsize.x < 57){
		  return new Point(9,qrsize.x);
	  }else if (qrsize.x >= 57 && qrsize.x < 61){
		  return new Point(10,qrsize.x);
	  }else if (qrsize.x >= 61 && qrsize.x < 65){
		  return new Point(11,qrsize.x);
	  }else if (qrsize.x >= 65 && qrsize.x < 69){
		  return new Point(12,qrsize.x);
	  }else if (qrsize.x >= 69 && qrsize.x < 73){
		  return new Point(13,qrsize.x);
	  }else if (qrsize.x >= 73 && qrsize.x < 77){
		  return new Point(14,qrsize.x);
	  }else if (qrsize.x >= 77 && qrsize.x < 81){
		  return new Point(15,qrsize.x);
	  }else if (qrsize.x >= 81 && qrsize.x < 85){
		  return new Point(16,qrsize.x);
	  }else if (qrsize.x >= 85 && qrsize.x < 89){
		  return new Point(17,qrsize.x);
	  }else if (qrsize.x >= 89 && qrsize.x < 93){
		  return new Point(18,qrsize.x);
	  }else if (qrsize.x >= 93 && qrsize.x < 97){
		  return new Point(19,qrsize.x);
	  }else if (qrsize.x >= 97 && qrsize.x < 101){
		  return new Point(20,qrsize.x);
	  }else if (qrsize.x >= 101 && qrsize.x < 105){
		  return new Point(21,qrsize.x);
	  }else if (qrsize.x >= 105 && qrsize.x < 109){
		  return new Point(22,qrsize.x);
	  }else if (qrsize.x >= 109 && qrsize.x < 113){
		  return new Point(23,qrsize.x);
	  }else if (qrsize.x >= 113 && qrsize.x < 117){
		  return new Point(24,qrsize.x);
	  }else if (qrsize.x >= 117 && qrsize.x < 121){
		  return new Point(25,qrsize.x);
	  }else if (qrsize.x >= 121 && qrsize.x < 125){
		  return new Point(26,qrsize.x);
	  }else if (qrsize.x >= 125 && qrsize.x < 129){
		  return new Point(27,qrsize.x);
	  }else if (qrsize.x >= 129 && qrsize.x < 133){
		  return new Point(28,qrsize.x);
	  }else if (qrsize.x >= 133 && qrsize.x < 137){
		  return new Point(29,qrsize.x);
	  }else if (qrsize.x >= 137 && qrsize.x < 141){
		  return new Point(30,qrsize.x);
	  }else if (qrsize.x >= 141 && qrsize.x < 145){
		  return new Point(31,qrsize.x);
	  }else if (qrsize.x >= 145 && qrsize.x < 149){
		  return new Point(32,qrsize.x);
	  }else if (qrsize.x >= 149 && qrsize.x < 153){
		  return new Point(33,qrsize.x);
	  }else if (qrsize.x >= 153 && qrsize.x < 157){
		  return new Point(34,qrsize.x);
	  }else if (qrsize.x >= 157 && qrsize.x < 161){
		  return new Point(35,qrsize.x);
	  }else if (qrsize.x >= 161 && qrsize.x < 165){
		  return new Point(36,qrsize.x);
	  }else if (qrsize.x >= 165 && qrsize.x < 169){
		  return new Point(37,qrsize.x);
	  }else if (qrsize.x >= 169 && qrsize.x < 173){
		  return new Point(38,qrsize.x);
	  }else if (qrsize.x >= 173 && qrsize.x < 177){
		  return new Point(39,qrsize.x);
	  }else if (qrsize.x >= 177){
		  return new Point(40,qrsize.x);
	  }else{
		  return new Point(0,25);
	  }
  }
  /**
   * This method detects a code in a "pure" image -- that is, pure monochrome image
   * which contains only an unrotated, unskewed, image of a code, with some white border
   * around it. This is a specialized method that works exceptionally fast in this special
   * case.
   *
   * @see com.google.zxing.datamatrix.DataMatrixReader#extractPureBits(BitMatrix)
   */
  private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {

    int[] leftTopBlack = image.getTopLeftOnBit();
    int[] rightBottomBlack = image.getBottomRightOnBit();
    if (leftTopBlack == null || rightBottomBlack == null) {
      throw NotFoundException.getNotFoundInstance();
    }

    float moduleSize = moduleSize(leftTopBlack, image);

    int top = leftTopBlack[1];
    int bottom = rightBottomBlack[1];
    int left = leftTopBlack[0];
    int right = rightBottomBlack[0];
    
    // Sanity check!
    if (left >= right || top >= bottom) {
      throw NotFoundException.getNotFoundInstance();
    }

    if (bottom - top != right - left) {
      // Special case, where bottom-right module wasn't black so we found something else in the last row
      // Assume it's a square, so use height as the width
      right = left + (bottom - top);
    }

    int matrixWidth = Math.round((right - left + 1) / moduleSize);
    int matrixHeight = Math.round((bottom - top + 1) / moduleSize);
    if (matrixWidth <= 0 || matrixHeight <= 0) {
      throw NotFoundException.getNotFoundInstance();
    }
    if (matrixHeight != matrixWidth) {
      // Only possibly decode square regions
      throw NotFoundException.getNotFoundInstance();
    }

    // Push in the "border" by half the module width so that we start
    // sampling in the middle of the module. Just in case the image is a
    // little off, this will help recover.
    int nudge = (int) (moduleSize / 2.0f);
    top += nudge;
    left += nudge;
    
    // But careful that this does not sample off the edge
    // "right" is the farthest-right valid pixel location -- right+1 is not necessarily
    // This is positive by how much the inner x loop below would be too large
    int nudgedTooFarRight = left + (int) ((matrixWidth - 1) * moduleSize) - right;
    if (nudgedTooFarRight > 0) {
      if (nudgedTooFarRight > nudge) {
        // Neither way fits; abort
        throw NotFoundException.getNotFoundInstance();
      }
      left -= nudgedTooFarRight;
    }
    // See logic above
    int nudgedTooFarDown = top + (int) ((matrixHeight - 1) * moduleSize) - bottom;
    if (nudgedTooFarDown > 0) {
      if (nudgedTooFarDown > nudge) {
        // Neither way fits; abort
        throw NotFoundException.getNotFoundInstance();
      }
      top -= nudgedTooFarDown;
    }

    // Now just read off the bits
    BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);
    for (int y = 0; y < matrixHeight; y++) {
      int iOffset = top + (int) (y * moduleSize);
      for (int x = 0; x < matrixWidth; x++) {
        if (image.get(left + (int) (x * moduleSize), iOffset)) {
          bits.set(x, y);
        }
      }
    }
    return bits;
  }

  private static float moduleSize(int[] leftTopBlack, BitMatrix image) throws NotFoundException {
    int height = image.getHeight();
    int width = image.getWidth();
    int x = leftTopBlack[0];
    int y = leftTopBlack[1];
    boolean inBlack = true;
    int transitions = 0;
    while (x < width && y < height) {
      if (inBlack != image.get(x, y)) {
        if (++transitions == 5) {
          break;
        }
        inBlack = !inBlack;
      }
      x++;
      y++;
    }
    if (x == width || y == height) {
      throw NotFoundException.getNotFoundInstance();
    }
    return (x - leftTopBlack[0]) / 7.0f;
  }

}
