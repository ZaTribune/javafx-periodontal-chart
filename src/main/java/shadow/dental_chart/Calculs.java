/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shadow.dental_chart;

/*
 * @author Muhammad Ali Arafah
 */
public class Calculs {

    public static void main(String[] args) {
        float mean;
        int sum, i;
        int a[] = {0,3, 2, 1};
        sum = 0;

        for (i = 0; i < a.length; i++) {
            sum += a[i];
        }
        System.out.println("Mean ::" + sum / (float) 192);
    }

}
