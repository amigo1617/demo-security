# security settings

```
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }
```

* AuthenticationProvider-retrieveUser 에서는 유저정보 조회만
* AuthenticationProvider-additionalAuthenticationChecks 에서 비번체크 및 기타 후처리


```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.accessDecisionManager(this.accessDecisionManager())
    }
    
    ....
    
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AffirmativeBased(List.of(new WebExpressionVoter(), this.CustomAccessDecisionVoter()));
    }
```
* AffirmativeBased인 경우 WebExpressionVoter는 permitAll() 만 핸들링, 나머지는 denyAll() 로 해야 CustomAccessDecisionVoter 에서 디시젼 매니징을 할수 있음 (.authorizeRequests().antMatchers("/test", "/api/**").permitAll().anyRequest().denyAll())
* denyAll() 대신 athenticated() 사용시 로그인 한 경우 WebExpressionVoter가 true 를 반환하므로 CustomAccessDecisionVoter까지 핸들이 넘어가질 않아 메뉴 권한을 필터링이 불가함

```
    .and()
        .sessionManagement()
        .invalidSessionUrl("/login")
        .maximumSessions(1)
        .maxSessionsPreventsLogin(false)
        .sessionRegistry(new SessionRegistryImpl())
        .expiredUrl("/login");
```
* 동접 제한은 UserDetail 의 hash , equals 를 아래와 같이 오버라이딩 한다
```
	@Override
	public int hashCode() {
		return Objects.hash(this.webId);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object instanceof CmmSessionVO) {
			return this.webId.equals(((CmmSessionVO) object).webId);
		} else {
			return false;
		}
	}
```
