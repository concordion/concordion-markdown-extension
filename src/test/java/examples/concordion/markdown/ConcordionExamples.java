package examples.concordion.markdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(MarkdownExtension.class)
public class ConcordionExamples {

    private int memory;

	public int add(int a, int b) {
		return a + b;
	}

	public void setMemory(int x) {
	    memory = x;
	}

    public int addToMemory(int x) {
        memory += x;
        return memory;
    }

    public InvoiceDetail getInvoiceDetail() {
        return new InvoiceDetail(100, 15);
    }

    public static class InvoiceDetail {

        public final int subTotal;  // to demonstrate field access
        private final int gst;      // to demonstrate getter method access

        public InvoiceDetail(int subTotal, int gst) {
            this.subTotal = subTotal;
            this.gst = gst;
        }

        public int getGst() {
            return gst;
        }

        public int calculateTotal() {
            return subTotal + gst;
        }
    }

    public Map<String, Integer> getInvoiceDetailAsMap() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("subTotal", 100);
        map.put("gst", 15);
        return map;
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        ArrayList<InvoiceDetail> list = new ArrayList<InvoiceDetail>();
        list.add(new InvoiceDetail(100, 15));
        list.add(new InvoiceDetail(500, 75));
        list.add(new InvoiceDetail(20, 3));
        return list;
    }

    public String setAndReturn(String name) {
    	// in a real case you'd do something with name and get the output
    	return name;
    }

    public String getBrowserDetails() {
        return "Firefox 3.6";
    }
}
