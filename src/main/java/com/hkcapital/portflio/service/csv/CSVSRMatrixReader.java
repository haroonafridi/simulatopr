package com.hkcapital.portflio.service.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVSRMatrixReader implements Reader<String, List<StrategyPositionRecords>>
{
    private static final int COLUMN_COUNT = 28;
    private final Path file;

    public CSVSRMatrixReader(Path file)
    {
        this.file = file;
    }

    @Override
    public List<StrategyPositionRecords> upload(String s)
    {
        List<StrategyPositionRecords> srMatrixRecords = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file))
        {

            // Read and skip header
            String header = reader.readLine();

            if (header == null)
            {
                return srMatrixRecords;
            }

            String line;

            while ((line = reader.readLine()) != null)
            {

                if (line.isBlank())
                {
                    continue;
                }

                String[] values = line.split(",", -1);

                if (values.length < COLUMN_COUNT)
                {
                    throw new IllegalArgumentException(
                            "Expected at least " + COLUMN_COUNT +
                                    " columns, got " + values.length +
                                    ": " + line
                    );
                }

                StrategyPositionRecords instrument = new StrategyPositionRecords(

                        values[0].trim(),     // STRATEGY_NAME

                        values[1].trim(),     // STRATEGY_DESC

                        Double.parseDouble(values[2].trim()),     // CAPITAL_ALLOCATED

                        Boolean.parseBoolean(values[3].trim()),     // STRATEGY_ACTIVE

                        values[4].trim(),     // INSTRUMENT

                        values[5].trim(),     // INSTUMENT_NAME

                        values[6].trim(),     // INSTRUMENT_DESC

                        values[7].trim(),     // URL

                        Integer.parseInt(values[8].trim()), // ETORO_ID

                        Integer.parseInt(values[9].trim()), // TIME_FRAME

                        values[10].trim(),     // TIME_FRAME_UNIT

                        Double.parseDouble(values[11].trim()), // SLIPPAGE

                        Double.parseDouble(values[12].trim()), // AMOUNT

                        Integer.parseInt(values[13].trim()), // LEV

                        values[14].trim(),    // POSITION_TYPE

                        Double.parseDouble(values[15].trim()), // L_SUPPORT

                        Double.parseDouble(values[16].trim()), // SUPPORT

                        Double.parseDouble(values[17].trim()), // R_SUPPORT

                        Double.parseDouble(values[18].trim()), // L_RESISTANCE

                        Double.parseDouble(values[19].trim()), // RESISTANCE

                        Double.parseDouble(values[20].trim()), // R_RESISTANCE

                        Double.parseDouble(values[21].trim()), // TAKE_PROFIT

                        Double.parseDouble(values[22].trim()), // STOP_LOSS

                        Integer.parseInt(values[23].trim()), // EXECUTION_COUNT

                        Boolean.parseBoolean(values[24].trim()), // ACTIVE

                        Boolean.parseBoolean(values[25].trim()), // WITH_FEED

                        Boolean.parseBoolean(values[26].trim()), // WITH_BAND

                        Boolean.parseBoolean(values[27].trim())  // WITH_CANDLE
                );

                srMatrixRecords.add(instrument);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return srMatrixRecords;
    }
}


