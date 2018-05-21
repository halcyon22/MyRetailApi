# MyRetailApi
Target Recruiting case study.

## Possible Improvements

### Abstract Product Details datasource
1. Create abstract ProductDetailsApiClient interface.
1. Make RedSkyApiClient implement it.
1. Configure by Spring profile.

### Replace existing Price storage
```
{
	"productId": 1,
	"currency_code": "USD",
	"prices": [{
		"start_date": "1900-01-01",
		"price": 15.99
	},{
		"start_date": "2018-01-01",
		"price": 12.99
	}]
}
```

### Add HATEOAS Support
https://spring.io/guides/gs/rest-hateoas/
