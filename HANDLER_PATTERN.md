#### Handler Pattern Implementation
To configure dental chart rows. Each row type (1-15) has a dedicated handler responsible for UI setup, event binding, and calculation callbacks.
1. **ForkRowHandler.java**
   - Handles rows 4 and 15
   - Features: Button cycling (0→3), icon styling, implant binding
   - Reusable for both fork button sets

2. **BleedingRowHandler.java**
   - Handles rows 5 and 14
   - Features: 3-button set handling, SVG styling, calculation callbacks
   - Reusable for both bleeding sets

3. **PlaqueRowHandler.java**
   - Handles rows 6 and 13
   - Features: 3-button set handling, SVG styling, percentage calculation
   - Reusable for both plaque sets

4. **GumWidthRowHandler.java**
   - Handles row 7
   - Features: Slider value binding, gum width property management

5. **GingivalMarginRowHandler.java**
   - Handles rows 8 and 12
   - Features: NumericFieldValidator integration, bidirectional binding
   - Reusable for both margin sets

6. **ProbingDepthRowHandler.java**
   - Handles rows 9 and 11
   - Features: NumericFieldValidator integration, depth calculations
   - Reusable for both depth sets

