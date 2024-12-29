package com.aquariux.trading_platform.service;

import com.aquariux.trading_platform.entity.AggregatedPrice;
import com.aquariux.trading_platform.model.BinanceResponse;
import com.aquariux.trading_platform.repository.AggregatedPriceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PriceAggregationService {

    private final AggregatedPriceRepository aggregatedPriceRepository;
    private final RestTemplate restTemplate;

    public PriceAggregationService(AggregatedPriceRepository aggregatedPriceRepository) {
        this.aggregatedPriceRepository = aggregatedPriceRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<AggregatedPrice> getLatestPrice() {
        return aggregatedPriceRepository.findAll();
    }

    public AggregatedPrice findByTradingPair(String tradingPair) {
        return aggregatedPriceRepository.findByTradingPair(tradingPair);
    }

    public void updatePrices(){
        aggregatedPriceRepository.saveAllAndFlush(fetchPriceFromBinance());
    }


    private List<AggregatedPrice> fetchPriceFromBinance() {
        String url = "https://api.binance.com/api/v3/ticker/bookTicker";
        var response = restTemplate.getForObject(url, BinanceResponse[].class);
        if (response == null) {
            return Collections.emptyList();
        }
        return extractPriceFromBinance(response, new String[]{"BTCUSDT", "ETHUSDT"});
    }

    private List<AggregatedPrice> extractPriceFromBinance(BinanceResponse[] binanceResponse, String[] tradingPair) {
        var now = LocalDateTime.now();
        return Arrays.stream(binanceResponse)
                .filter(x -> Arrays.stream(tradingPair).anyMatch(pair -> pair.equals(x.getSymbol())))
                .map(el ->  new AggregatedPrice(el.getSymbol(), el.getBidPrice(), el.getAskPrice(), now))
                .collect(Collectors.toList());

    }



}
