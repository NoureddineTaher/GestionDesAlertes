package fr.real.supervision.appliinfo.web.util;

import fr.real.supervision.appliinfo.web.status.config.MeteoStatusDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ToolStatusAndSeverity {
    public static String toolStatusAndSeverity(List<MeteoStatusDto> meteoStatuses)
    {
        HashMap<String, Boolean> result=new HashMap<>();
        for(MeteoStatusDto meteoStatus : meteoStatuses){
            if(Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Fatal")){
                result.put("Fatal_A",true);
                break;
            }
            if(!Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Fatal")){
                result.put("Fatal",true);
                continue;
            }
            if(Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Critical")){
               result.put("Critical_A",true);
                continue;
            }
            if(!Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Critical")){
                result.put("Critical",true);
                continue;
            }
            if(Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Minor")){
                result.put("Minor_A",true);
                continue;
            }
            if(!Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Minor")){
                result.put("Minor",true);
                continue;
            }
            if(Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Warning")){
                result.put("Warning_A",true);
                continue;
            }
            if(!Objects.equals(meteoStatus.getTicket().getStatus(), "A") && Objects.equals(meteoStatus.getTicket().getSeverity(), "Warning")){
               result.put("Warning",true);

            }
            else{
                result.put("OK",true);

            }


        }

        if(!Objects.isNull(result.get("Fatal_A"))){
            return "Fatal_A";
        }
        if(!Objects.isNull(result.get("Fatal"))){
            return "Fatal";
        }
        if(!Objects.isNull(result.get("Critical_A"))){
            return "Critical_A";
        }
        if(!Objects.isNull(result.get("Critical"))){
            return "Critical";
        }
        if(!Objects.isNull(result.get("Minor_A"))){
            return "Minor_A";
        }
        if(!Objects.isNull(result.get("Minor"))){
            return "Minor";
        }
        if(!Objects.isNull(result.get("Warning_A"))){
            return "Warning_A";
        }
        if(!Objects.isNull(result.get("Warning"))){
            return "Warning";
        }
        return "OK";
    }
}
